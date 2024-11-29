# 导入需要的库
## warnings 抑制警告信息。
from warnings import simplefilter
simplefilter(action='ignore', category=FutureWarning)
## pandas 用于数据读取与处理。
import pandas as pd
## sklearn 提供模型训练、测试、评估工具，包括随机森林分类器、ROC曲线计算。
from sklearn.model_selection import train_test_split
## seaborn 和 matplotlib.pyplot：用于绘制图表
import seaborn as sns
import matplotlib.pyplot as plt
from sklearn import metrics
from sklearn.metrics import roc_curve, auc
## numpy：数值计算。
import numpy as np
from sklearn.ensemble import RandomForestClassifier, GradientBoostingClassifier

# =============读取csv数据===========
def Read_data(file):
    dt = pd.read_csv(file)
    dt.columns = ['age', 'sex', 'chest_pain_type', 'resting_blood_pressure', 'cholesterol','fasting_blood_sugar', 'rest_ecg', 'max_heart_rate_achieved','exercise_induced_angina','st_depression', 'st_slope', 'num_major_vessels', 'thalassemia', 'target']
    data =dt
    return data

# ===========数据清洗==============
def data_clean(data):
    # 重复值处理
    print('存在' if any(data.duplicated()) else '不存在', '重复观测值')
    data.drop_duplicates()

    # 缺失值处理
    print('不存在' if any(data.isnull()) else '存在', '缺失值')
    data.dropna()  # 直接删除记录
    data.fillna(method='ffill')  # 前向填充
    data.fillna(method='bfill')  # 后向填充
    data.fillna(value=2)  # 值填充
    data.fillna(value={'resting_blood_pressure': data['resting_blood_pressure'].mean()})  # 统计值填充

    # 异常值处理
    data1 = data['resting_blood_pressure']
    # 标准差监测
    xmean = data1.mean()
    xstd = data1.std()
    print('存在' if any(data1 > xmean + 2 * xstd) else '不存在', '上限异常值')
    print('存在' if any(data1 < xmean - 2 * xstd) else '不存在', '下限异常值')
    # 箱线图监测
    q1 = data1.quantile(0.25)
    q3 = data1.quantile(0.75)
    up = q3 + 1.5 * (q3 - q1)
    dw = q1 - 1.5 * (q3 - q1)
    print('存在' if any(data1 > up) else '不存在', '上限异常值')
    print('存在' if any(data1 < dw) else '不存在', '下限异常值')
    data1[data1 > up] = data1[data1 < up].max()
    data1[data1 < dw] = data1[data1 > dw].min()
    return data

#==============数据编码=============
def data_encoding(data):
    data = data[["age", 'sex', "chest_pain_type", "resting_blood_pressure", "cholesterol","fasting_blood_sugar", "rest_ecg","max_heart_rate_achieved", "exercise_induced_angina","st_depression", "st_slope", "num_major_vessels","thalassemia","target"]]
    Discretefeature=['sex',"chest_pain_type", "fasting_blood_sugar", "rest_ecg","exercise_induced_angina",  "st_slope", "thalassemia"]
    Continuousfeature=["age", "resting_blood_pressure", "cholesterol","max_heart_rate_achieved","st_depression","num_major_vessels"]

    df = pd.get_dummies(data,columns=Discretefeature)

    df[Continuousfeature]=(df[Continuousfeature]-df[Continuousfeature].mean())/(df[Continuousfeature].std())
    df["target"]=data[["target"]]
    return df

#=============数据集划分==============
def data_partition(data):
    # 1.4查看样本是否平衡
    print(data["target"].value_counts())
    # X提取变量特征；Y提取目标变量
    X = data.drop('target', axis=1)
    y = data['target']
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=.2,random_state=10)
    feature=list(X.columns)
    return X_train, y_train, X_test, y_test,feature

#===========绘制ROC曲线================
def Draw_ROC(list1,list2):
    fpr_model,tpr_model,thresholds=roc_curve(list1,list2,pos_label=1)
    roc_auc_model=auc(fpr_model,tpr_model)

    font = {'family': 'Times New Roman','size': 12,}
    sns.set(font_scale=1.2)
    plt.rc('font',family='Times New Roman')

    plt.plot(fpr_model,tpr_model,'blue',label='AUC = %0.2f'% roc_auc_model)
    plt.legend(loc='lower right',fontsize = 12)
    plt.plot([0,1],[0,1],'r--')
    plt.ylabel('True Positive Rate',fontsize = 14)
    plt.xlabel('Flase Positive Rate',fontsize = 14)
    plt.show()
    return

#==============随机森林=================
def RF(X_train, y_train, X_test, y_test,feature):
    rf1 = RandomForestClassifier(max_depth=5, n_estimators=100, random_state=0)
    rf1.fit(X_train, y_train)
    print("\nFinally results of RF fitting:")
    print("Accuracy on training set: {:.3f}".format(rf1.score(X_train, y_train)))
    print("Accuracy on test set: {:.3f}".format(rf1.score(X_test, y_test)))
    print("Feature importance:\n{}".format(rf1.feature_importances_))
    predict_target=rf1.predict(X_test)
    predict_target_prob=rf1.predict_proba(X_test)  # 输出分类概率
    predict_target_prob_rf=predict_target_prob[:,1]
    df = pd.DataFrame({'prob':predict_target_prob_rf,'target':predict_target,'labels':list(y_test)})

    print('预测正确的数量:')
    print(sum(predict_target==y_test))

    print('RF测试集:')
    print(metrics.classification_report(y_test,predict_target))
    print(metrics.confusion_matrix(y_test, predict_target))

    print('RF训练集:')
    predict_Target=rf1.predict(X_train)
    print(metrics.classification_report(y_train,predict_Target))
    print(metrics.confusion_matrix(y_train, predict_Target))

    id=np.argwhere(rf1.feature_importances_>=0)
    id=[i for item in id for i in item]
    dic={}
    for i in id:
        dic.update({feature[i]:rf1.feature_importances_[i]})
    df=pd.DataFrame.from_dict(dic,orient='index',columns=['权重'])
    df=df.reset_index().rename(columns={'index':'特征'})
    df=df.sort_values(by='权重',ascending=False)

    data_hight=df['权重'].values.tolist()
    data_x=df['特征'].values.tolist()

    font = {'family': 'Times New Roman','size': 7}
    sns.set(font_scale=1.2)
    plt.rc('font',family='Times New Roman')

    plt.figure(figsize=(8,8))
    plt.barh(range(len(data_x)), data_hight, color='#6699CC')
    plt.yticks(range(len(data_x)),data_x,fontsize=12)

    plt.tick_params(labelsize=12)
    plt.xlabel('Feature importance',fontsize=14)
    plt.title("GB feature importance analysis",fontsize = 14)
    plt.show()
    return list(y_test), list(predict_target_prob_rf)

if __name__=="__main__":
    data1=Read_data("D:\python\python-code\Dataset.csv")
    data1=data_clean(data1)
    data2=data_encoding(data1)
    X_train, y_train, X_test, y_test,feature= data_partition(data2)

    y_test,predict_target_prob_rf=RF(X_train, y_train, X_test, y_test,feature)
    Draw_ROC(y_test,predict_target_prob_rf)