from Server.Objects import User
from Server.Objects import Object
from Server.Objects import Status
from Server.Profile import Comment
from enum import Enum
import time
# status: 0 - in_search
# status: 1 - in_process
# status: 2 - is_done


class EventAdvert(Enum):
    WitcherCreator = "AdvertActivityWitcher"
    ClientAsWitcher = "ClientReplaceWitcher"
    AlienDelete = "AlienClientDeleteContract"
    Success = "Success"

class Advert(Enum):
    TaskLocated = "id_task_located"
    Text = "text"
    Bounty = "bounty"
    Photo = "photo"
    NewPhoto = "new_photo"
    DelPhoto = "del_photo"
    ID = "id"
    Status = "status"
    Witcher = "id_witcher"


def create_advert(cursor, params):
    cursor.execute("select * from Client where id_profile=(select id_profile from Token_Table where token='{}')"
                   .format(params[User.Token.value]))
    row = cursor.fetchone()

    obj = Object()
    status = Object()

    if row is not None:
        id_client = row[1]

        cursor.execute("insert into List_Comments (empty) values(null)")
        cursor.execute("select max(id) from List_Comments")
        id_lcomment = cursor.fetchone()[0]

        cursor.execute("insert into List_Photos (empty) values(null)")
        cursor.execute("select max(id) from List_Photos")
        id_lphoto = cursor.fetchone()[0]

        if params.get(Advert.Photo.value, None) is not None:
            lst = params[Advert.Photo.value]
            for photo in lst:
                cursor.execute("insert into Photo (id_list_photos, photo) values({}, '{}')"
                               .format(id_lphoto, photo))

        cur_time = int(time.time())
        cursor.execute("insert into Contract (id_witcher, id_client, id_list_comments, id_task_located, \
                        id_list_photos, text, bounty, status, last_update_status, last_update) \
                         values(null, {}, {}, {}, {}, N'{}', {}, {}, {}, {})"
                       .format(id_client, id_lcomment, params[Advert.TaskLocated.value], id_lphoto,
                               params[Advert.Text.value], params[Advert.Bounty.value], 0, cur_time, cur_time))

        status.status = Status.Ok.value
        obj.message = EventAdvert.Success.value
        cursor.execute("select max(id) from Contract")
        obj.id_advert = cursor.fetchone()[0]

    else:
        status.status = Status.Error.value
        obj.message = EventAdvert.WitcherCreator.value

    status.object = obj
    return status.toJSON()


def edit_advert(cursor, params):
    cursor.execute("select * from Client where id_profile=(select id_profile from Token_Table where token='{}')"
                   .format(params[User.Token.value]))
    row = cursor.fetchone()

    obj = Object()
    status = Object()

    if row is not None:
        cursor.execute("select * from Contract where id={}".format(params[Advert.ID.value]))
        row = cursor.fetchone()
        update_string = ""
        last_update = int(time.time())
        id_lphoto = row[5]

        if params.get(Advert.Witcher.value, None) is not None:
            update_string += "id_witcher={},".format(params[Advert.Witcher.value])
        if params.get(Advert.TaskLocated.value, None) is not None:
            update_string += "id_task_located={},".format(params[Advert.TaskLocated.value])
        if params.get(Advert.Text.value, None) is not None:
            update_string += "text=N'{}',".format(params[Advert.Text.value])
        if params.get(Advert.Bounty.value, None) is not None:
            update_string += "bounty={},".format(params[Advert.Bounty.value])
        if params.get(Advert.Status.value, None) is not None:
            update_string += "status={},".format(params[Advert.Status.value])
            update_string += "last_update_status={},".format(last_update)
        update_string += "last_update={}".format(last_update)

        if params.get(Advert.DelPhoto.value, None) is not None:
            lst = params[Advert.DelPhoto.value]
            for photo in lst:
                cursor.execute("delete Photo where token='{}'".format(photo))

        if params.get(Advert.NewPhoto.value, None) is not None:
            lst = params[Advert.NewPhoto.value]
            for photo in lst:
                cursor.execute("insert into Photo (id_list_photos, photo) values({}, '{}')".format(id_lphoto, photo))

        cursor.execute("update Contract set {} where id={}".format(update_string, params[Advert.ID.value]))

        status.status = Status.Ok.value
        obj.message = EventAdvert.Success.value
    else:
        status.status = Status.Error.value
        obj.message = EventAdvert.WitcherCreator.value

    status.object = obj
    return status.toJSON()


def delete_advert(cursor, params):
    cursor.execute("select * from Client where id_profile=(select id_profile from Token_Table where token='{}')"
                   .format(params[User.Token.value]))
    row = cursor.fetchone()

    obj = Object()
    status = Object()

    if row is not None:
        id_client = row[1]
        cursor.execute("select id_client from Contract where id={}".format(params[Advert.ID.value]))
        id_cont_client = cursor.fetchone()[0]

        if id_client == id_cont_client:
            cursor.execute("delete Contract where id={}".format(params[Advert.ID.value]))
            status.status = Status.Ok.value
            obj.message = EventAdvert.Success.value
        else:
            status.status = Status.Error.value
            obj.message = EventAdvert.AlienDelete.value
    else:
        status.status = Status.Error.value
        obj.message = EventAdvert.WitcherCreator.value

    status.object = obj
    return status.toJSON()


def get_advert(cursor, params):
    cursor.execute("select * from Contract where id={}".format(params[Advert.ID.value]))
    status = Object()
    obj = Object()

    row = cursor.fetchone()
    obj.id = row[0]
    obj.text = row[6]
    obj.bounty = row[7]
    obj.status = row[8]
    obj.last_update_status = row[9]
    obj.last_update = row[10]
    obj.header = row[11]
    id_witcher = row[1]
    id_client = row[2]
    id_list_comments = row[3]
    id_task_located = row[4]
    id_list_photos = row[5]

    if id_witcher is not None:
        cursor.execute("select id, name from Profile where id=(select id_profile from Witcher where id={})".format(id_witcher))
        row = cursor.fetchone()
        witcher = Object()
        witcher.id = row[0]
        witcher.name = row[1]
        status.witcher = witcher

    cursor.execute(
        "select id, name from Profile where id=(select id_profile from Client where id={})".format(id_client))
    row = cursor.fetchone()
    client = Object()
    client.id = row[0]
    client.name = row[1]
    status.client = client

    cursor.execute("select a.name, b.name from Town as a inner join Kingdom as b on a.id_kingdom=b.id where a.id={}"
                   .format(id_task_located))
    row = cursor.fetchone()
    obj.town = row[0]
    obj.kingdom = row[1]

    cursor.execute(
        "select b.photo from Contract as a inner join Photo as b on a.id_list_photos = b.id_list_photos where a.id={}"
        .format(id_list_photos))
    row = cursor.fetchall()
    obj.photoContract = Object()
    obj.photoContract.photo = {}
    obj.photoContract.count = len(row)
    for i in range(len(row)):
        ph = Object()
        ph.photo = row[i][0]
        obj.photoContract.photo[len(obj.photoContract.photo)] = ph

    status.object = obj
    return status.toJSON()


def add_witcher_in_contract(cursor, params):
    cursor.execute("select * from Witcher where id_profile=(select id_profile from Token_Table where token='{}')"
                   .format(params[User.Token.value]))
    row = cursor.fetchone()

    obj = Object()
    status = Object()

    if row is not None:
        id_witcher = row[0]
        status.status = Status.Ok.value
        obj.message = EventAdvert.Success.value

        cursor.execute("insert into Desired_Contract (id_witcher, id_contract) values({}, {})"
                       .format(id_witcher, params[Advert.ID.value]))
    else:
        status.status = Status.Error.value
        obj.message = EventAdvert.ClientAsWitcher.value

    status.object = obj
    return status.toJSON()


def get_profile_desired_contract(cursor, params):
    cursor.execute("select d.id, d.name from Contract as a inner join Desired_Contract as b on a.id=b.id_contract \
                    inner join Witcher as c on b.id_witcher=c.id inner \
                    join Profile as d on c.id_profile=d.id where a.id={}".format(params[Advert.ID.value]))
    rows = cursor.fetchall()

    status = Object()
    obj = Object()

    status.status = Status.Ok.value
    obj.message = EventAdvert.Success.value
    obj.witchers = Object()
    obj.witchers.witcher = {}
    obj.witchers.count = len(rows)
    for prof in rows:
        profile = Object()
        profile.id = prof[0]
        profile.name = prof[1]
        obj.witchers.witcher[len(obj.witchers.witcher)] = profile

    status.object = obj
    return status.toJSON()


def write_comment_contract(cursor, params):
    cursor.execute("select id_list_comments from Contract where id={}".format(params[Advert.ID.value]))
    id_lcomment = cursor.fetchone()[0]

    cursor.execute("insert into Comment (id_list_comment, text, create_date) values({}, N'{}', {})"
                   .format(id_lcomment, params[Comment.TextComment.value], int(time.time())))

    status = Object()
    status.status = Status.Ok.value

    return status.toJSON()
