from Server.Objects import Status
from Server.Objects import Object
from enum import Enum


class Profile(Enum):
    ID = "id"


def get_profile(cursor, params):
    cursor.execute(
        "select a.id, a.name, a.about, b.photo from Profile as a left join Photo as b on a.id_photo = b.id where a.id={}"
            .format(params[Profile.ID.value]))
    row = cursor.fetchone()

    status = Object()
    obj = Object()
    status.status = Status.Error.value

    if row is not None:
        status.status = Status.Ok.value
        obj.id = row[0]
        obj.name = row[1]
        obj.about = row[2]
        obj.photo = row[3]

        cursor.execute(
            "select b.text, b.create_date, b.[order] from Profile as a inner join Comment as b on a.id_list_comments = b.id_list_comment where a.id={}"
                .format(params[Profile.ID.value]))

        obj.commentsProfile = Object()
        row = cursor.fetchall()
        obj.commentsProfile.comments = {}
        obj.commentsProfile.count = len(row)
        for line in row:
            comm = Object()
            comm.text = line[0]
            comm.create_date = line[1]
            comm.order = line[2]
            obj.commentsProfile.comments[len(obj.commentsProfile.comments)] = comm

    status.object = obj
    return status.toJSON()
