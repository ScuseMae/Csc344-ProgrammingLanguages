# -----------------------------------------------
# Project: Main Project 5 - Python
#
# Description: Python program that collects,
#              summarizes, and e-mails all the
#              programming assignments for Csc344
#              course.
# -----------------------------------------------

import os
import sys
import zipfile
import getpass
import smtplib
import re
Regex_1 = "([a-zA-Z_][a-zA-Z0-9_]*)\s*[;=:(]"
Regex_2 = "\(*\[*([a-zA-Z_][\-a-zA-Z0-9_]*)\s*[;:(\]\[\ ]"
Regex_3 = "([a-zA-Z_][a-zA-Z0-9_]*)\s*[;=:(]"
Regex_4 = "([a-zA-Z_][a-zA-Z0-9_]*)\s*[;\-,=:(]"
Regex_5 = "([a-zA-Z_][a-zA-Z0-9_]*)\s*[;=(]"

# file_Names = ["Assign1C.c","Assign2Clojure.txt","Assign3Scala.scala","ancestor.pl","samplePython.py"]

# Directory -
userDirectory = raw_input('Directory:')
os.chdir(userDirectory)
print(os.listdir(userDirectory))


# Assignment 1 - C
AssignOne_C = open("Assign1C.c", "r")
Search_AssignOne = re.findall(Regex_1, AssignOne_C.read())
Identifiers_AssignOne = sorted(set(Search_AssignOne), key=lambda x: Search_AssignOne.index(x))

# Assignment 1 - C.html
html_AssignOne = '<br>'.join(Identifiers_AssignOne)
html_C = open("AssignOne_C.html", "w")
html_C.write("Assignment 1 - C <br><br>")
html_C.write(html_AssignOne)
html_C.close()


# Assignment 2 - Clojure
AssignTwo_Clojure = open("Assign2Clojure.txt", "r")
Search_AssignTwo = re.findall(Regex_2, AssignTwo_Clojure.read())
Identifiers_AssignTwo = sorted(set(Search_AssignTwo), key=lambda x: Search_AssignTwo.index(x))

# Assignment 2 - Clojure.html
html_AssignTwo = '<br>'.join(Identifiers_AssignTwo)
html_Clojure = open("Assign2_Clojure.html", "w")
html_Clojure.write("Assignment 2 - Clojure <br><br>")
html_Clojure.write(html_AssignTwo)
html_Clojure.close()


# Assignment 3 - Scala
AssignThree_Scala = open("Assign3Scala.txt", "r")
Search_AssignThree = re.findall(Regex_3, AssignThree_Scala.read())
Identifiers_AssignThree = sorted(set(Search_AssignThree), key=lambda x: Search_AssignThree.index(x))

# Assignment 3 - Scala.html
html_AssignThree = '<br>'.join(Identifiers_AssignThree)
html_Scala = open("Assign3_Scala.html", "w")
html_Scala.write("Assignment 3 - Scala <br><br>")
html_Scala.write(html_AssignThree)
html_Scala.close()


# Assignment 4 - Prolog
AssignFour_Prolog = open("Assign4Prolog.txt", "r")
Search_AssignFour = re.findall(Regex_4, AssignFour_Prolog.read())
Identifiers_AssignFour = sorted(set(Search_AssignFour), key=lambda x: Search_AssignFour.index(x))

# Assignment 4 - Prolog.html
html_AssignFour = '<br>'.join(Identifiers_AssignFour)
html_Prolog = open("Assign4_Prolog.html", "w")
html_Prolog.write("Assignment 4 - Prolog <br><br>")
html_Prolog.write(html_AssignFour)
html_Prolog.close()


# Assignment 5 - Python
AssignFive_Python = open("Assign5Python.txt", "r")
Search_AssignFive = re.findall(Regex_5, AssignFive_Python.read())
Identifiers_AssignFive = sorted(set(Search_AssignFive), key=lambda x: Search_AssignFive.index(x))

# Assignment 5 - Python.html
html_AssignFive = '<br>'.join(Identifiers_AssignFive)
html_Python = open("Assign5_Python.html", "w")
html_Python.write("Assignment 5 - Python <br><br>")
html_Python.write(html_AssignFive)
html_Python.close()


# Index_html File
Index_html = open("Index.html", "w")
Index_html.write("Csc 344 Assignment Index <br><br>")
Index_html.write('Assignment 1 - C: <a href = "Assign1C.c">  Source</a>   [148 Lines]  <a href = "AssignOne_C.html">  html</a>  [39 Lines] <br>')
Index_html.write('Assignment 2 - Clojure: <a href = "Assign2Clojure.txt"> Source</a>   [105 Lines]  <a href = "Assign2_Clojure.html">  html</a>  [52 Lines] <br>')
Index_html.write('Assignment 3 - Scala: <a href = "Assign3Scala.txt"> Source</a>   [192 Lines]  <a href = "Assign3_Scala.html">  html</a>  [48 Lines] <br>')
Index_html.write('Assignment 4 - Prolog: <a href = "Assign4Prolog.txt"> Source</a>   [72 Lines]  <a href = "Assign4_Prolog.html">  html</a>  [51 Lines] <br>')
Index_html.write('Assignment 5 - Python: <a href = "Assign5Python.txt"> Source</a>   [100 Lines]  <a href = "Assign5_Python.html">  html</a>  [81 Lines] <br>')
Index_html.close()


# Zip-file
fileName = "Csc344_Assign5.tar"
zip_archive = zipfile.ZipFile(fileName, "w")
zip_archive.write("Assign1C.c")
zip_archive.write("AssignOne_C.html")
zip_archive.write("Assign2Clojure.txt")
zip_archive.write("Assign2_Clojure.html")
zip_archive.write("Assign3Scala.txt")
zip_archive.write("Assign3_Scala.html")
zip_archive.write("Assign4Prolog.txt")
zip_archive.write("Assign4_Prolog.html")
zip_archive.write("Assign5Python.txt")
zip_archive.write("Assign5_Python.html")
zip_archive.write("Index.html")
zip_archive.close()


# Email
from email.mime.text import MIMEText
from email.mime.image import MIMEImage
from email.mime.multipart import MIMEMultipart
from email.utils import COMMASPACE, formatdate
from email.mime.base import MIMEBase
from email import encoders

email_Sender = 'senderEmail@address.com'
email_ToReceiver = raw_input('To Receiver: ')

email_Header = MIMEMultipart()
email_Header['Subject '] = "Csc344 Assignment 5 Python"
email_Header['From '] = email_Sender
email_Header['To '] = email_ToReceiver
email_Header['Date'] = formatdate(localtime = True)

email_Content = MIMEText("Zip-file containing all Csc344 Assignment sources, along with corresponding html summaries and links.")
email_Header.attach(email_Content)

attachment_Name = "Assign5Python.zip"
email_Attachment = open("DirectoryToFile", "rb")
part = MIMEBase('application', 'octet-stream')
part.set_payload((email_Attachment).read())
part.add_header('Content-Disposition', "email_Attachment; filename= %s" % attachment_Name)
email_Header.attach(part)

email_ServerPort = smtplib.SMTP('smtp.gmail.com', 587)
email_ServerPort.ehlo()
email_ServerPort.starttls()
passWord = getpass.getpass()
email_ServerPort.login(email_Sender, passWord)
email_ServerPort.sendmail(email_Sender, email_ToReceiver, email_Header.as_string())
print("Email Successfully Sent...")

