from Server.Objects import Object
from Server.Objects import Status
from Server.Advert import Advert
from Server.Profile import User
from Server.Advert import create_advert
from Server.FCM import send_firebase_push
from enum import Enum
Advert.IDpost = "id_contract"


class EventWitcher(Enum):
    Success = "Success"
    WitcherSelect = "WitcherSelectWork"
    ClientSelect = "ClientSelectStatus"


class Witcher(Enum):
    ID = "id"
    IDpost = "id_witcher"
    Status = "status"


def select_witcher(cursor, params):
    cursor.execute("select * from Client where id_profile=(select id_profile from Token_Table where token='{}')"
                   .format(params[User.Token.value]))
    row = cursor.fetchone()
    status = Object()

    if row is not None:
        cursor.execute("select id from Witcher where id_profile={}".format(params[Witcher.IDpost.value]))
        id_witcher = cursor.fetchone()[0]
        cursor.execute("update Contract set id_witcher={} where id={}"
                       .format(id_witcher, params[Advert.IDpost]))

        cursor.execute("select header from Contract where id={}".format(params[Advert.IDpost]))
        cont = cursor.fetchone()[0]
        title = 'Вы выбраны для выполнения контракта!'
        body = 'Контракт ' + cont
        cursor.execute("select id_profile from Witcher where id={}".format(params[Witcher.IDpost.value]))
        id_sender = cursor.fetchone()[0]
        send_firebase_push(cursor, body, title, id_sender)

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
        cursor.execute("select id_client, header from Contract where id={}".format(params[Advert.IDpost]))
        row = cursor.fetchone()
        id_sender = row[0]
        cont = row[1]
        cursor.execute("select name from Profile where id=(select id_profile from Token_Table where token='{}')"
                       .format(params[User.Token.value]))
        name = cursor.fetchone()[0]
        body = 'Контракт ' + cont

        if id_answer == 2:
            cursor.execute("update Contract set status=2 where id={}".format(params[Advert.IDpost]))
            title = 'Ведьмак ' + name + ' взялся за контракт!'
        else:
            cursor.execute("update Contract set id_witcher=null, status=0 where id={}".format(params[Advert.IDpost]))
            title = 'Ведьмак ' + name + ' отказался от контракта!'

        send_firebase_push(cursor, body, title, id_sender)

        status.status = Status.Ok.value
        status.message = EventWitcher.Success.value
    else:
        status.status = Status.Error.value
        status.message = EventWitcher.ClientSelect.value

    return status.toJSON()


def refuse_contract(cursor, params):
    # создать копию объявления в зависимости от отказа и выставить статус
    st = 4
    cursor.execute("select * from Client where id_profile=(select id_profile from Token_Table where token='{}')"
                   .format(params[User.Token.value]))
    row = cursor.fetchone()
    if row is None:
        cursor.execute("select * from ")
        #token
    else:
        st = 5




    status = Object()
    cursor.execute("update Contract set id_witcher=null status=0 where id={}"
                   .format(params[Advert.IDpost]))
    status.status = Status.Ok.value
    status.message = EventWitcher.Success.value

    return status.toJSON()


def contract_complited(cursor, params):
    cursor.execute("select * from Witcher where id_profile=(select id_profile from Token_Table where token='{}')"
                   .format(params[User.Token.value]))
    row = cursor.fetchone()
    status = Object()

    if row is not None:
        cursor.execute('update Contract set status=3 where id={}'.format(params[Advert.ID.value]))
        status.status = Status.Ok.value

        cursor.execute('select id_client, header from Contract where id={}'.format(params[Advert.ID.value]))
        row = cursor.fetchone()
        id_sender = row[0]
        title = 'Контракт ' + row[1]
        cursor.execute("select name from Profile where id=(select id_profile from Token_Table where token='{}')"
                       .format(params[User.Token.value]))
        name = cursor.fetchone()[0]
        body = 'Ведьмак ' + name + ' выполнил выше задание!'
        send_firebase_push(cursor, title, body, id_sender)
    else:
        status.status = Status.Error.value

    return status.toJSON()
