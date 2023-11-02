import pandas
import pandas as pd
import json

excel_path="C:\\Users\\14748\\Desktop\\3-4年级英语.xlsx"
column_name="单词"

df=pd.read_excel(excel_path)

data_dict={}

for index,value in enumerate(df[column_name],1):
    data_dict[index]=value

print(data_dict)
file_name="C:\\Users\\14748\\Desktop\\question.txt"

with open(file_name, 'w') as json_file:
    json.dump(data_dict, json_file, indent=4)