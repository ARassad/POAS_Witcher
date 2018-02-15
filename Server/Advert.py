from Server.Objects import User
from Server.Objects import Object
from Server.Objects import Status
from enum import Enum
import time
# status: 0 - in_search
# status: 1 - in_process
# status: 2 - is_done


class EventAdvert(Enum):
    WitcherCreator = "AdvertCreatedWitcher"
    Success = "Success"

class Advert(Enum):
    TaskLocated = "id_task_located"
    Text = "text"
    Bounty = "bounty"
    Photo = "photo"


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

        cur_time = time.time().__int__()
        cursor.execute("insert into Contract (id_witcher, id_client, id_list_comments, id_task_located, id_list_photos, text, bounty, status, last_update_status, last_update) values(null, {}, {}, {}, {}, N'{}', {}, {}, {}, {})"
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