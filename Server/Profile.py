from Server.Objects import Status
from Server.Objects import Object
from Server.Objects import User
from Server.Objects import Comment
from enum import Enum
import time


class Profile(Enum):
    ID = "id"
    Name = "name"
    Photo = "photo"
    About = "about"
    Password = "password"


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


def update_profile(cursor, params):
    cursor.execute("select id_profile from Token_Table where token='{}'".format(params[User.Token.value]))
    id_prof = cursor.fetchone()[0]
    cursor.execute("select * from Profile where id={}".format(id_prof))
    prof = cursor.fetchone()
    id_photo = prof[3]
    id_auth = prof[1]

    update_string = ""
    if params.get(Profile.About.value, None) is not None:
        update_string += "about=N'{}',".format(params[Profile.About.value])
    if params.get(Profile.Name.value, None) is not None:
        update_string += "name=N'{}'".format(params[Profile.Name.value])
    if update_string != "":
        if update_string[-1] == ',':
            update_string = update_string[:-1]

        cursor.execute("update Profile set {} where id={}".format(update_string, id_prof))

    if params.get(Profile.Photo.value, None) is not None:
        if id_photo is None:
            cursor.execute("insert into Photo (id_list_photos, photo) values(null, {})".format(params[Profile.Photo.value]))
            cursor.execute("select max(id) from Photo")
            id_photo = cursor.fetchone()[0]
            cursor.execute("update Profile set id_photo={} where id={}".format(id_photo, id_prof))
        else:
            cursor.execute("update Photo set photo={} where id={}".format(params[Profile.Photo.value], id_photo))

    if params.get(Profile.Password.value, None) is not None:
        cursor.execute("update Authorization_info set password={} where id={}".format(params[Profile.Password.value], id_auth))

    status = Object()
    status.status = Status.Ok.value

    return status.toJSON()


def write_comment_profile(cursor, params):
    cursor.execute("select id_list_comments from Profile where id={}".format(params[Profile.ID.value]))
    id_lcomment = cursor.fetchone()[0]

    cursor.execute("insert into Comment (id_list_comment, text, [order], create_date) values({}, N'{}', {}, {})"
                   .format(id_lcomment, params[Comment.TextComment.value],
                           params[Comment.OrderID.value], time.time().__int__()))

    status = Object()
    status.status = Status.Ok.value

    return status.toJSON()