from Server.Objects import Object, Status
from enum import Enum


class EventGetListContracts(Enum):
    SuccessGetListContracts = "Success"
    IncorrectParams = "IncorrectParams : "
    MinParamMiss = "ParamMinMissing"
    MaxParamMiss = "ParamMaxMissing"
    TownParamMiss = "townParamMissing"
    FilterLocateErr = "MissingTownandKingdomParams"
    SortErr = "ParamsSortError"


class Params:

    class Filter:
        Name = "filter"
        Bounty = "bounty"
        Locate = "locate"

    class Sort:
        Name = "sort"
        Alph = "alph"
        Locate = "locate"
        LastUpdate = "lastupdate"

    class SortType:
        Name = "sortype"
        Asc = "asc"
        Desc = "desc"

    Min = "min"
    Max = "max"
    Town = "town"
    Kingdom = "kingdom"


def get_list_contracts(cursor, params):
    req = "select * from Contract"
    status = Object()
    obj = Object()
    status.status = Status.Ok.value

    filtr = params.get(Params.Filter.Name)
    if filtr is not None:
        req += " where"
        if filtr == Params.Filter.Bounty:
            min = params.get(Params.Min)
            max = params.get(Params.Max)

            if min is None or max is None:
                status.status = Status.Error.value
                obj.value = "ERROR:{}{}".format(EventGetListContracts.MinParamMiss if min is None else "",
                                                EventGetListContracts.MaxParamMiss if max is None else "").value
            else:
                req += " Bounty > {} and Bounty < {}".format(min, max)

        elif filtr == Params.Filter.Locate:
            town = params.get(Params.Town)
            kingdom = params.get(Params.Kingdom)

            if town is None and kingdom is None:
                status.status = Status.Error.value
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

    sort = params.get(Params.Sort.Name)
    if sort is not None:
        req += " order by"
        if sort == Params.Sort.Alph:
            req += " text"
        elif sort == Params.Sort.Locate:
            req += " id_task_located"
        elif sort == Params.Sort.LastUpdate:
            req += " last_update"
        else:
            status.status = Status.Error.value
            obj.value = EventGetListContracts.SortErr.value

        sort_type = params.get(Params.SortType.Name)
        if sort_type is not None and sort_type == Params.SortType.Desc:
            req += " desc"

    if status.status == Status.Ok.value:
        cursor.execute(req)
        contracts = cursor.fetchall()

        obj.contracts = {}
        for n, i in enumerate(contracts):
            contr = Object()

            contr.id = i[0]
            contr.id_witcher = i[1]
            contr.id_client = i[2]
            contr.id_list_comments = i[3]
            contr.id_task_located = i[4]
            contr.id_list_photos = i[5]
            contr.text = i[6]
            contr.bounty = i[7]
            contr.status = i[8]
            contr.last_update_status = i[9]
            contr.last_update = i[10]
            contr.header = i[11]

            obj.contracts[n] = contr

    status.object = obj
    return status.toJSON()
