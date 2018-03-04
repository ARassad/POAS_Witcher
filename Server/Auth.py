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
        obj.phone_number = '+' + row[3][1:]
        status.message = EventAuth.SuccessAuthorizaion.value
        status.status = Status.Ok.value
        #tok = params[User.Login.value] + str(time.time())
        #obj.token = md5(tok.encode('utf-8')).hexdigest()

        cursor.execute("select id from Profile where id_authorization_info={}".format(id_auth))
        row = cursor.fetchone()
        obj.id_profile = row[0]
        #cursor.execute("insert into Token_Table (token, last_update, id_profile) values('{}', {}, {})"
        #               .format(obj.token, int(time.time()), obj.id_profile))

        #  Вставка fcm_tokena
        cursor.execute("select * from FCM_Token where id_profile = {}".format(obj.id_profile))
        fcm = cursor.fetchone()
        if fcm is not None:
            cursor.execute("delete FCM_Token where id_profile = {}".format(obj.id_profile))
        cursor.execute("insert into FCM_Token (id_profile, fcm_token) values( {}, '{}')"
                       .format(obj.id_profile, params[User.FCM_Token.value]))

        cursor.execute("select * from Witcher where id_profile={}".format(obj.id_profile))
        row = cursor.fetchone()
        if row is not None:
            obj.type = 0
        else:
            obj.type = 1

        status.object = obj
    else:
        status.message = EventAuth.PasswordIncorrect.value

    return status.toJSON()


def set_token(cursor, params):
    cursor.execute("select id from Profile where id_authorization_info=(select id from Authorization_info \
                    where phone_number='{}')".format(params["phone_number"]))
    id_prof = cursor.fetchone()[0]
    cursor.execute("insert into Token_Table(token, last_update, id_profile) values('{}', {}, {})"
                   .format(params[User.Token.value], int(time.time()), id_prof))
    status = Object()
    status.status = Status.Ok.value
    return status.toJSON()
