Class Descriptions

dataAnalyzer - Analyzes data. This is where student data is analyzed with Google Maps APIs. Main usage is finding compatibility between students.

Student - the student object that contains kids arrival and departure times, as well as addresses.

Edge - The object that connects students to each other in graph theory.

csvTester - simple tester class, most things commented don't work anymore

dsaStudents.txt - list of students and their data that I'm using

dsaPID.txt - created local list of place_ids, so I can use less API requests


Student Data needs to be in this format:

IMPORT_ID	firstName	lastName	grade	streetNumber streetName	City	State	zipCode

Edit filenames in dataAnalyzer to match your tab separated file of students

	(This file can be created in Excel by saving it as a tab delimited file)