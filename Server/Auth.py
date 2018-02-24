from Server.Objects import User
from Server.Objects import Object
from Server.Objects import Status
from enum import Enum
from hashlib import md5
import time


class EventAuth(Enum):
    SuccessAuthorizaion = "Success"
    LoginNotExist = "LoginNotExist"
    PasswordIncorrect = "IncorrectPasswd"


def authorization(cursor, params):
    cursor.execute("select * from Authorization_info where login='{}'".format(params[User.Login.value]))
    row = cursor.fetchone()

    status = Object()
    obj = Object()
    status.status = Status.Error.value

    if row is None:
        status.message = EventAuth.LoginNotExist.value
    elif row[2] == params[User.Password.value]:
        id_auth = row[0]
        status.message = EventAuth.SuccessAuthorizaion.value
        status.status = Status.Ok.value
        tok = params[User.Login.value] + str(time.time())
        obj.token = md5(tok.encode('utf-8')).hexdigest()

        cursor.execute("select id from Profile where id_authorization_info={}".format(id_auth))
        obj.id_profile = cursor.fetchone()[0]
        cursor.execute("insert into Token_Table (token, last_update, id_profile) values('{}', {}, {})"
                       .format(obj.token, int(time.time()), obj.id_profile))

        #  Вставка fcm_tokena
        cursor.execute("select * from FCM_Token where id_profile = {}".format(obj.id_profile))
        if len(cursor.fetchone()) != 0:
            cursor.execute("delete FCM_Token where id_profile = {}".format(obj.id_profile))
        cursor.execute("insert into FCM_Token (id_profile, fcm_token) values( {}, '{}')"
                       .format(obj.id_profile, params[User.FCM_Token.value]))

        status.object = obj
    else:
        status.message = EventAuth.PasswordIncorrect.value

    return status.toJSON()
