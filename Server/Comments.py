from Server.Objects import Object
from Server.Objects import Status
from Server.Profile import Profile
from enum import Enum


class Comments(Enum):
    Type = "type"
    Profile = "profile"
    Contract = "contract"


class EventComments(Enum):
    Success = "Success"


def get_list_comment(cursor, params):
    sel = "Contract"
    if params.get(Comments.Type.value, None) == Comments.Profile.value:
        sel = "Profile"
    cursor.execute("select text, create_date, id from Comment where id_list_comment=(select id_list_comments from {} where id={})"
                    .format(sel, params[Profile.ID.value]))
    row = cursor.fetchall()

    status, obj = Object(), Object()
    status.status = Status.Ok.value
    obj.message = EventComments.Success.value
    obj.id_comments = {}
    for i in row:
        comm = Object()
        comm.text = i[0]
        comm.date = i[1]

        cursor.execute("select a.id, a.name, b.photo from Profile as a left join Photo as b on a.id_photo = b.id where a.id_list_comments=(select id_list_comment from Comment where id={})"
                       .format(i[2]))
        prof = cursor.fetchone()
        comm.id_prof = prof[0]
        comm.name = prof[1]
        comm.photo = prof[2]
        obj.id_comments[len(obj.id_comments)] = comm

    status.object = obj
    return status.toJSON()
