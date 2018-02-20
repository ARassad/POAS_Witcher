from Server.Objects import Object
from Server.Objects import Status


def get_towns(cursor, params):
    cursor.execute("select * from Kingdom")
    row = cursor.fetchall()

    status = Object()
    status.status = Status.Ok.value
    status.count_kingdom = len(row)
    status.kingdoms = {}
    for i in row:
        cursor.execute("select * from Town where id_kingdom={}".format(i[0]))
        towns = cursor.fetchall()
        kingdoms = i[1]
        status.kingdoms[kingdoms] = Object()
        status.kingdoms[kingdoms].count_town = len(towns)
        status.kingdoms[kingdoms].town = {}
        for j in towns:
            town = Object()
            town.id_town = j[0]
            town.name_town = j[1]
            status.kingdoms[kingdoms].town[len(status.kingdoms[kingdoms].town)] = town

    return status.toJSON()
