from Server.Objects import Object
from Server.Objects import Status
from Server.Objects import User
from enum import Enum


class EventRegistration(Enum):
    SuccessRegistration = "Success"
    LoginExist = "LoginExist"


def registration(cursor, params):
    cursor.execute("select * from Authorization_info where login='{}'".format(params[User.Login.value]))
    row = cursor.fetchone()

    obj = Object()
    status = Object()

    if row is None:
        status.status = Status.Ok.value
        obj.message = EventRegistration.SuccessRegistration.value

        cursor.execute("insert into Authorization_info (login, password) values('{}', '{}')"
                       .format(params[User.Login.value], params[User.Password.value])
                       )
        cursor.execute("select max(id) from Authorization_info")
        row = cursor.fetchone()
        obj.idAuth = row[0]

        cursor.execute("insert into List_Comments (empty) values(null)")
        cursor.execute("select max(id) from List_Comments")
        row = cursor.fetchone()
        obj.idLComment = row[0]

        cursor.execute("insert into Profile (id_authorization_info, id_list_comments, id_photo, name, about) values({}, {}, null, null, null)".format(obj.idAuth, obj.idLComment))
        cursor.execute("select max(id) from Profile;")
        row = cursor.fetchone()
        obj.idProf = row[0]

        if params.get(User.IsWitcher.value, None) == 'True':
            cursor.execute("insert into Witcher (id_profile) values({})".format(obj.idProf))
            obj.isWitcher = True
        else:
            cursor.execute("insert into Client (id_profile) values({})".format(obj.idProf))
            obj.isWitcher = False

    else:
        status.status = Status.Error.value
        obj.message = EventRegistration.LoginExist.value

    status.object = obj
    return status.toJSON()
