from Server.Objects import User
from Server.Objects import Object
from Server.Objects import Status
from enum import Enum


class EventAuth(Enum):
    SuccessAuthorizaion = "Success"
    LoginNotExist = "LoginNotExist"
    PasswordIncorrect = "IncorrectPasswd"


def authorization(cursor, params, ret_bool=False):
    cursor.execute("select * from Authorization_info where login='{}'".format(params[User.Login.value]))
    row = cursor.fetchone()

    status = Object()
    obj = Object()
    status.status = Status.Error.value

    if row is None:
        obj.message = EventAuth.LoginNotExist.value
    elif row[2] == params[User.Password.value]:
        obj.message = EventAuth.SuccessAuthorizaion.value
        status.status = Status.Ok.value
    else:
        obj.message = EventAuth.PasswordIncorrect.value

    if ret_bool:
        return status.status == Status.Ok.value

    status.object = obj
    return status.toJSON()
