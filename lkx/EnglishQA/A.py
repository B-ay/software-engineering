import pandas
import pandas as pd
import json
import random

excel_path="C:\\Users\\14748\\Desktop\\3-4年级英语.xlsx"
column_name="A"

df=pd.read_excel(excel_path)

data_dict={}

for index,value in enumerate(df[column_name],1):
    data_dict[index]=value

print(data_dict)

key=data_dict.keys()
right=list(data_dict.values())
print(type(right))
valueA=[]
valueB=[]
valueC=[]
valueD=[]
ans=[]
mask=len(right)
print(mask)
for i in key:
    i=i-1
    print(i)
    #答案所在的列表，1-A,2-B,3-C,4-D
    target=random.randint(1,4)
    #随机其他错误答案
    other=random.randint(1,10)
    if(target==1):
        valueA.append(right[i])
        valueB.append(right[(i+other+mask)%mask])
        valueC.append(right[(i + other*2+mask) % mask])
        valueD.append(right[(i + other*3+mask) % mask])
        ans.append('A')
    elif (target == 2):
        valueB.append(right[i])
        valueA.append(right[(i + other+mask) % mask])
        valueC.append(right[(i + other * 2+mask) % mask])
        valueD.append(right[(i + other * 3+mask) % mask])
        ans.append('B')
    elif (target == 3):
        valueC.append( right[i])
        valueA.append( right[(i + other+mask) % mask])
        valueB.append( right[(i + other * 2+mask) % mask])
        valueD.append( right[(i + other * 3+mask) % mask])
        ans.append('C')
    else:
        valueD.append( right[i])
        valueA.append( right[(i + other+mask) % mask])
        valueB.append( right[(i + other * 2+mask) % mask])
        valueC.append( right[(i + other * 3+mask) % mask])
        ans.append( 'D')

A={index+1:value for index,value in enumerate(valueA) }
B={index+1:value for index,value in enumerate(valueB) }
C={index+1:value for index,value in enumerate(valueC) }
D={index+1:value for index,value in enumerate(valueD) }
ANS={index+1:value for index,value in enumerate(ans) }
file_nameA="C:\\Users\\14748\\Desktop\\A.txt"
file_nameB="C:\\Users\\14748\\Desktop\\B.txt"
file_nameC="C:\\Users\\14748\\Desktop\\C.txt"
file_nameD="C:\\Users\\14748\\Desktop\\D.txt"
file_ans="C:\\Users\\14748\\Desktop\\ans.txt"

with open(file_nameA, 'w',encoding="utf-8") as json_file:
    json.dump(A, json_file,ensure_ascii=False, indent=4)
with open(file_nameB, 'w',encoding="utf-8") as json_file:
    json.dump(B, json_file,ensure_ascii=False, indent=4)
with open(file_nameC, 'w',encoding="utf-8") as json_file:
    json.dump(C, json_file,ensure_ascii=False, indent=4)
with open(file_nameD, 'w',encoding="utf-8") as json_file:
    json.dump(D, json_file,ensure_ascii=False, indent=4)
with open(file_ans, 'w',encoding="utf-8") as json_file:
    json.dump(ANS, json_file,ensure_ascii=False, indent=4)

# ans="C:\\Users\\14748\\Desktop\\ans.txt"
# with open(file_nameA, 'w',encoding="utf-8") as json_file:
#     json.dump(data_dict, json_file,ensure_ascii=False,indent=4)