from Server.Objects import Object, Status
from enum import Enum
import json

class EventGetListContracts(Enum):
    SuccessGetListContracts = "Success"
    IncorrectParams = "IncorrectParams : "
    MinParamMiss = "ParamMinMissing"
    MaxParamMiss = "ParamMaxMissing"
    TownParamMiss = "townParamMissing"
    FilterLocateErr = "MissingTownandKingdomParams"


class Params:

    class Filter:
        Name = "filter"
        Bounty = "bounty"
        Locate = "locate"
        allValues = [Bounty, Locate]

    Min = "min"
    Max = "max"
    Town = "town"
    Kingdom = "kingdom"


def get_list_contracts(cursor, params):
    req = "select * from Contract"
    obj = Object()
    obj.status = Status.Ok.value

    filtr = params.get(Params.Filter.Name)
    if filtr is not None:
        req += " where"
        if filtr == Params.Filter.Bounty:
            min = params.get(Params.Min)
            max = params.get(Params.Max)

            if min is None or max is None:
                obj.status = Status.Error.value
                obj.value = "ERROR:{}{}".format(EventGetListContracts.MinParamMiss if min is None else "",
                                                EventGetListContracts.MaxParamMiss if max is None else "").value
            else:
                req += "Bounty > {} and Bounty < {}".format(min, max)

        elif filtr == Params.Filter.Locate:
            town = params.get(Params.Town)
            kingdom = params.get(Params.Kingdom)

            if town is None and kingdom is None:
                obj.status = Status.Error.value
                obj.value = EventGetListContracts.FilterLocateErr.value
            else:
                req += " Contract.id_task_located in (select id from Town where"

                if town is not None:
                    req += " Town.name = '{}'".format(town)

                    if kingdom is not None:
                        req += " and"
                    else:
                        req += ')'

                if kingdom is not None:
                    req += " Town.id_kingdom in (select id from Kingdom where Kingdom.name = '{}'))".format(kingdom)

    if obj.status == Status.Ok.value:
        cursor.execute(req)
        obj.contracts = []
        for i in cursor.fetchall():
            obj.contracts.append(list(i))

    return obj.toJSON()
