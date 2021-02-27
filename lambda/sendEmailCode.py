import boto3, os, json
import smtplib
import email.message
from boto3.dynamodb.conditions import Key, Attr

url_expiration = 172800 # 2days

def sendEmail(to, subject, body):

    gmail_sender = os.environ['email_account']
    gmail_passwd = os.environ['email_password']
    print(gmail_passwd)
    msg = email.message.Message()
    msg['Subject'] = subject
    msg['From'] = gmail_sender
    msg['To'] = to
    msg.add_header('Content-Type','text/html')
    msg.set_payload(body)

    # Gmail Sign In

    server = smtplib.SMTP('smtp.gmail.com', 587)
    server.ehlo()
    server.starttls()
    server.login(gmail_sender, gmail_passwd)

    print(msg)
    try:
        server.sendmail(gmail_sender, [to], msg.as_string())
        print ('email sent to ' + to)
    except:
        print ('error sending mail to ' + to)

    return server.quit()

def lambda_handler(event, context):

    for record in event['Records']:
        payload=record["body"]
        print(payload)
        payload = json.loads(payload)

        if payload["token"] == "12345":
            to = payload["to"]
            subject = payload["subject"]
            body = payload["body"]
            sendEmail(to, subject, body)

    return "done"
