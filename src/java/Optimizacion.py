import scipy.optimize as io
import numpy as np
import gc
#modelo = open('C:/Users/auron/Desktop/Proyectos/Optimizacion/modelo.txt', 'r')
modelo = open('/opt/tomcat9/webapps/asignacion_academica/WEB-INF/classes/modelo.txt', 'r')
lineas = modelo.readlines()
objetivo = lineas[0][:-1]
c = list(map(int, objetivo.split(',')))

restricciones = lineas[1][:-1]
restpyc = restricciones.split(';')
A=[[]]
for rest in restpyc:
    vec=rest.split(',')
    if A==[[]]:
        A=list(map(int, vec))
    else:
        A = np.vstack([A, list(map(int, vec))])

coheficientes = lineas[2][:-1]
b = list(map(int, coheficientes.split(',')))

xmatrix = lineas[3]
xlist = xmatrix.split(';')
x=[[]]
for xline in xlist:
    vec=xline.split(',')
    for index, item in enumerate(vec):
        if item == 'None':
            vec[index] = None
    if x==[[]]:
        x=vec
    else:
        x = np.vstack([x, vec])

gc.collect()
z = io.linprog(c, A, b, bounds=(x))

#resultado = open('C:/Users/auron/Desktop/Proyectos/Optimizacion/resultado.txt', 'w')
resultado = open('/opt/tomcat9/webapps/asignacion_academica/WEB-INF/classes/resultado.txt', 'w')
cad=''
for var in z.x:
    cad=cad+str(var)+','
resultado.write(cad)