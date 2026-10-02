import re

with open('Proyecto_Proframacion_grupo02/src/main/java/interfaces/AltaUsuario.java', 'r') as f:
    content = f.read()

# I want to insert password prompting logic in btnAceptarActionPerformed before step 2.

replacement = """        if (nickname.isEmpty() || email.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Por favor, complete todos los campos de texto obligatorios.",
                    "Campos Incompletos",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // --- INGRESO DE CONTRASEÑA ---
        javax.swing.JPasswordField pf1 = new javax.swing.JPasswordField();
        javax.swing.JPasswordField pf2 = new javax.swing.JPasswordField();
        Object[] message = {
            "Contraseña:", pf1,
            "Confirmar Contraseña:", pf2
        };
        
        int option = javax.swing.JOptionPane.showConfirmDialog(this, message, "Ingreso de Contraseña", javax.swing.JOptionPane.OK_CANCEL_OPTION);
        if (option != javax.swing.JOptionPane.OK_OPTION) {
            return;
        }
        
        String pass1 = new String(pf1.getPassword());
        String pass2 = new String(pf2.getPassword());
        
        if (pass1.isEmpty() || !pass1.equals(pass2)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden o están vacías", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        // ------------------------------
"""

content = content.replace('''        // 3. Validación de campos obligatorios de texto
        if (nickname.isEmpty() || email.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Por favor, complete todos los campos de texto obligatorios.",
                    "Campos Incompletos",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }''', replacement)

# Now, we also need to pass the password to IController when creating the user.
# Let's see how IController is called. I need to find the controller method for creating users.
with open('Proyecto_Proframacion_grupo02/src/main/java/interfaces/AltaUsuario.java', 'w') as f:
    f.write(content)

