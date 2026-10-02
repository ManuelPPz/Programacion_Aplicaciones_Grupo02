import re

with open('Proyecto_Proframacion_grupo02/src/main/java/Logica/IController.java', 'r') as f:
    ic = f.read()
ic = ic.replace(
    'public abstract void AgregarUsuario(String nickname, String nombre, String apellido, String correo, Date fechaNac, boolean docente, List<String> institutos, String imgPath)throws Exception;',
    'public abstract void AgregarUsuario(String nickname, String nombre, String apellido, String correo, String password, Date fechaNac, boolean docente, List<String> institutos, String imgPath)throws Exception;'
)
with open('Proyecto_Proframacion_grupo02/src/main/java/Logica/IController.java', 'w') as f:
    f.write(ic)


with open('Proyecto_Proframacion_grupo02/src/main/java/Logica/Controller.java', 'r') as f:
    c = f.read()

c = c.replace(
    'public void AgregarUsuario(String nickname, String nombre, String apellido, String correo, Date fechaNac, boolean docente, List<String> institutos, String imgPath)throws Exception {',
    'public void AgregarUsuario(String nickname, String nombre, String apellido, String correo, String password, Date fechaNac, boolean docente, List<String> institutos, String imgPath)throws Exception {'
)
c = c.replace('String password = GenerateRandPassword();', '// String password = GenerateRandPassword();')

with open('Proyecto_Proframacion_grupo02/src/main/java/Logica/Controller.java', 'w') as f:
    f.write(c)

with open('Proyecto_Proframacion_grupo02/src/main/java/interfaces/AltaUsuario.java', 'r') as f:
    au = f.read()

au = au.replace(
    'ico.AgregarUsuario(nickname, nombre, apellido, email, fecha, true, institutos, imgPath);',
    'ico.AgregarUsuario(nickname, nombre, apellido, email, pass1, fecha, true, institutos, imgPath);'
)
au = au.replace(
    'ico.AgregarUsuario(nickname, nombre, apellido, email, fecha, false, null, imgPath);',
    'ico.AgregarUsuario(nickname, nombre, apellido, email, pass1, fecha, false, null, imgPath);'
)
with open('Proyecto_Proframacion_grupo02/src/main/java/interfaces/AltaUsuario.java', 'w') as f:
    f.write(au)
