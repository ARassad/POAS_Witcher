from urllib.request import Request, urlopen
ApplicationID = 'AAAA1RkXo8Y:APA91bE7Lq22gQq6gY8Fnznu6ZDGMUfg-8bIYfpRU87RM1YWgysOtwbegF9yBpcVyqEuWpg-kV2djF1zuq7RbtOOm0rSIqJfbJgikXTchkZCKOlRnGVu8PYdzhCv52Gm6e07dP2Q7a17'
SenderID = '915249013702'

def send_firebase_push(cursor, title, body, id_profile):
    import json

    cursor.execute('select fcm_token from FCM_Token where id_profile={}'.format(id_profile))
    row = cursor.fetchone()
    if row is not None:
        url = 'https://fcm.googleapis.com/fcm/send'  # Set destination URL here
        post_fields = {
            'to': row[0],
            'notification': {
                'title': title,
                'body': body,
                'icon': 'witcher_icon',
                'sound': 'default'
            }
        }  # Set POST fields here
        val = json.dumps(post_fields)
        request = Request(url, val.encode(), headers={'Content-Type': 'application/json', 'Authorization': 'key={}'
                          .format(ApplicationID), 'Sender': 'id={}'.format(SenderID)})
        json = json.loads(urlopen(request).read().decode())
        print(json)
    else:
        cursor.execute('insert into Push_Message(id_profile, title, body) values({}, {}, {})'
                       .format(id_profile, title, body))
        print('push to queue')
