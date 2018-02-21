from Server.Objects import Object
from Server.Objects import Status
from Server.Advert import Advert
from Server.Profile import User
from enum import Enum
Advert.IDpost = "id_contract"


class EventWitcher(Enum):
    Success = "Success"
    WitcherSelect = "WitcherSelectWork"
    ClientSelect = "ClientSelectStatus"


class Witcher(Enum):
    ID = "id"
    IDpost = "id_witcher"
    Status = "Status"


def select_witcher(cursor, params):
    cursor.execute("select * from Client where id_profile=(select id_profile from Token_Table where token='{}')"
                   .format(params[User.Token.value]))
    row = cursor.fetchone()
    status = Object()

    if row is not None:
        cursor.execute("update Contract set id_witcher={} where id={}"
                      .format(params[Witcher.IDpost.value], params[Advert.IDpost]))

        status.status = Status.Ok.value
        status.message = EventWitcher.Success.value
    else:
        status.status = Status.Ok.value
        status.message = EventWitcher.WitcherSelect.value

    return status.toJSON()


def answer_witcher(cursor, params):
    cursor.execute("select * from Witcher where id_profile=(select id_profile from Token_Table where token='{}')"
                   .format(params[User.Token.value]))
    row = cursor.fetchone()
    status = Object()

    if row is not None:
        id_answer = params[Witcher.Status.value]
        if id_answer == 1:
            cursor.execute("update Contract set status={} where id={}"
                           .format(params[Witcher.Status.value], params[Advert.IDpost]))
        else:
            cursor.execute("update Contract set id_witcher=null where id={}"
                           .format(params[Advert.IDpost]))
        status.status = Status.Ok.value
        status.message = EventWitcher.Success.value
    else:
        status.status = Status.Error.value
        status.message = EventWitcher.ClientSelect.value

    return status.toJSON()


def refuse_contract(cursor, params):
    cursor.execute("select * from Client where id_profile=(select id_profile from Token_Table where token='{}')"
                   .format(params[User.Token.value]))
    row = cursor.fetchone()
    status = Object()

    if row is not None:
        cursor.execute("update Contract set id_witcher=null where id={}"
                       .format(params[Advert.IDpost]))
        status.status = Status.Ok.value
        status.message = EventWitcher.Success.value
    else:
        status.status = Status.Error.value
        status.message = EventWitcher.WitcherSelect.value

    return status.toJSON()
