from Server.Objects import Object
from Server.Objects import Status
from Server.Advert import Advert
from Server.Profile import User
from enum import Enum
Advert.IDpost = "id_contract"


class EventWitcher(Enum):
    Success = "Success"
    WitcherSelect = "WitcherSelectWork"


class Witcher(Enum):
    ID = "id"
    IDpost = "id_witcher"


def select_witcher(cursor, params):
    cursor.execute("select * from Client where id_profile=(select id_profile from Token_Table where token='{}')"
                   .format(params[User.Token.value]))
    row = cursor.fetchone()
    status, obj = Object(), Object()

    if row is not None:
        cursor.execute("update Contract set id_witcher={} where id={}"
                      .format(params[Witcher.IDpost.value], params[Advert.IDpost]))

        status.status = Status.Ok.value
        obj.message = EventWitcher.Success.value
    else:
        status.status = Status.Ok.value
        obj.message = EventWitcher.WitcherSelect.value

    status.object = obj
    return status.toJSON()
