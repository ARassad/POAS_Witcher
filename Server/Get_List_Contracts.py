from Server.Objects import Object, Status
from enum import Enum
from Server.Objects import User


class EventGetListContracts(Enum):
    SuccessGetListContracts = "Success"
    IncorrectParams = "IncorrectParams : "
    MinParamMiss = "ParamMinMissing"
    MaxParamMiss = "ParamMaxMissing"
    TownParamMiss = "townParamMissing"
    StatusParamMiss = "statusParamMissing"
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
    Status = "status"


def get_list_contracts(cursor, params):
    return list_contract(cursor, params)


def get_contract_client(cursor, params):
    cursor.execute("select * from Client where id_profile=(select id_profile from Token_Table where token='{}')"
                   .format(params[User.Token.value]))
    row = cursor.fetchone()

    return list_contract(cursor, params, id_client=row[0])


def get_contract_witcher(cursor, params):
    cursor.execute("select * from Witcher where id_profile=(select id_profile from Token_Table where token='{}')"
                   .format(params[User.Token.value]))
    row = cursor.fetchone()

    return list_contract(cursor, params, id_witcher=row[0])


def list_contract(cursor, params, **kwargs):
    obj = Object()
    status = Object()
    status.status = Status.Error.value
    obj.message = "Неопознанная ошибка в методу Get List Contracts"

    req = 'select * from Contract'
    req += " inner join \
                (select town, kingdom, idT from ((select [Town].name as town, [Town].id_kingdom, \
                [Town].id as idT from [Town] ) as T\
                inner join \
                (select [Kingdom].name as kingdom, [Kingdom].id as idK From [Kingdom]) as K on T.id_kingdom = K.idK) )\
                 as TK on id_task_located = TK.idT"

    if kwargs.get('id_witcher') is not None:
        req += ' inner join Desired_Contract on Desired_Contract.id_contract = Contract.id \
                 where Desired_Contract.id_witcher={}'.format(kwargs.get('id_witcher'))
    elif kwargs.get('id_client') is not None:
        req += ' where id_client={}'.format(kwargs.get('id_client'))

    filtr = params.get(Params.Filter.Name)
    if filtr is not None:
        req += " and "
        if filtr == Params.Filter.Bounty:
            min = params.get(Params.Min)
            max = params.get(Params.Max)

            if min is None or max is None:
                status.status = Status.Error.value
                obj.value = "ERROR:{}{}".format(EventGetListContracts.MinParamMiss if min is None else "",
                                                EventGetListContracts.MaxParamMiss if max is None else "")
            else:
                req += " Bounty > {} and Bounty < {}".format(min, max)

        elif filtr == Params.Filter.Locate:
            town = params.get(Params.Town)
            kingdom = params.get(Params.Kingdom)

            if town is None and kingdom is None:
                status.status = Status.Error.value
                obj.value = EventGetListContracts.FilterLocateErr.value
            else:
                if town is not None:
                    req += " town = '{}' {} ".format(town, "and " if kingdom is not None else "")

                if kingdom is not None:
                    req += " kingdom = '{}' ".format(kingdom)

    stat = params.get(Params.Status)
    if stat is not None and (kwargs.get('id_witcher') is not None or kwargs.get('id_client') is not None):
        req += " and status={}".format(stat)

    sort = params.get(Params.Sort.Name)

    if sort is not None:
        sort_type_desc = params.get(Params.SortType.Name) is not None \
                         and params.get(Params.SortType.Name) == Params.SortType.Desc

        req += " order by"
        if sort == Params.Sort.Alph:
            req += " header"
        elif sort == Params.Sort.Locate:
            req += " kingdom {}, town".format("desc" if sort_type_desc else "")
        elif sort == Params.Sort.LastUpdate:
            req += " last_update"
        else:
            status.status = Status.Error.value
            obj.value = EventGetListContracts.SortErr.value

        if sort_type_desc:
            req += " desc"

    cursor.execute(req)
    row = cursor.fetchall()
    headers = tuple(cursor.description)

    obj.contracts = {}
    for N, i in enumerate(row):

        line = Object()
        for n, head in enumerate(headers):
            setattr(line, head[0], i[n])

        obj.contracts[N] = line

    status.object = obj
    status.status = Status.Ok.value
    obj.message = EventGetListContracts.SuccessGetListContracts.value
    return status.toJSON()
