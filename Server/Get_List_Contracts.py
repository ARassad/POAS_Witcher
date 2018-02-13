from Server.Objects import Object, Status
from enum import Enum

class EventGetListContracts(Enum):
    SuccessGetListContracts = "Success"
    IncorrectParams = "IncorrectParams : "

    class Params:
        Filter = "filter"
        Min = "min"
        Max = "max"


def get_list_contracts(cursor, params):
    with params['filter']
    cursor.execute("select * from Contract")

    obj = Object()

    obj.contract = cursor.fetchall()
    obj.status = Status.Error.value

    return obj.toJSON()
