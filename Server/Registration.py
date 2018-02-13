from Server.Objects import Object
from Server.Objects import Status
from Server.Objects import User
from Server.Objects import EventRegistration

def registration(cursor, params):
    cursor.execute("select * from Authorization_info where login='{}'".format(params[User.Login.value]))
    row = cursor.fetchone()

    obj = Object()

    if row is None:
        obj.status = Status.Ok.value
        obj.value = EventRegistration.SuccessRegistration.value
        cursor.execute(
            "insert into Authorization_info (login, password) values('{}', '{}')".format(params[User.Login.value],
                                                                                             params[
                                                                                                 User.Password.value]))
        cursor.commit()
    else:
        obj.status = Status.Error.value
        obj.value = EventRegistration.LoginExist.value

    return obj.toJSON()
