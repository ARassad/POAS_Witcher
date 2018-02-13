from Server.Objects import EventAuth
from Server.Objects import User
from Server.Objects import Object
from Server.Objects import Status


def authorization(cursor, params):
    cursor.execute("select * from Authorization_info where login='{}'".format(params[User.Login.value]))
    row = cursor.fetchone()

    obj = Object()
    obj.status = Status.Error.value

    if row is None:
        obj.value = EventAuth.LoginNotExist.value
    elif row[2] == params[User.Password.value]:
        obj.value = EventAuth.SuccessAuthorizaion.value
        obj.status = Status.Ok.value
    else:
        obj.value = EventAuth.PasswordIncorrect.value

    return obj.toJSON()
