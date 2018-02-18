from Objects import Object
from Objects import Status
from Objects import User
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
        id_auth = row[0]

        cursor.execute("insert into List_Comments (empty) values(null)")
        cursor.execute("select max(id) from List_Comments")
        row = cursor.fetchone()
        id_lcomment = row[0]

        cursor.execute("insert into Profile (id_authorization_info, id_list_comments, id_photo, name, about) values({}, {}, null, null, null)".format(id_auth, id_lcomment))
        cursor.execute("select max(id) from Profile;")
        row = cursor.fetchone()
        id_prof = row[0]

        if params.get(User.IsWitcher.value, None) == 1:
            cursor.execute("insert into Witcher (id_profile) values({})".format(id_prof))
            obj.is_witcher = True
        else:
            cursor.execute("insert into Client (id_profile) values({})".format(id_prof))
            obj.is_witcher = False

    else:
        status.status = Status.Error.value
        obj.message = EventRegistration.LoginExist.value

    status.object = obj
    return status.toJSON()
