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
    id_auth = row[0]

    status = Object()
    obj = Object()
    status.status = Status.Error.value

    if row is None:
        status.message = EventAuth.LoginNotExist.value
    elif row[2] == params[User.Password.value]:
        status.message = EventAuth.SuccessAuthorizaion.value
        status.status = Status.Ok.value
        tok = params[User.Login.value] + str(time.time())
        obj.token = md5(tok.encode('utf-8')).hexdigest()

        cursor.execute("select id from Profile where id_authorization_info={}".format(id_auth))
        id_profile = cursor.fetchone()[0]
        cursor.execute("insert into Token_Table (token, last_update, id_profile) values('{}', {}, {})"
                       .format(obj.token, time.time().__int__(), id_profile))
        status.object = obj
    else:
        status.message = EventAuth.PasswordIncorrect.value

    return status.toJSON()
