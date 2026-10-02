import re

with open('PDA_WebServer/pom.xml', 'r') as f:
    content = f.read()

new_deps = """    <dependencies>
        <!-- Jakarta EE API -->
        <dependency>
            <groupId>jakarta.platform</groupId>
            <artifactId>jakarta.jakartaee-api</artifactId>
            <version>${jakartaee.version}</version>
            <scope>provided</scope>
        </dependency>

        <!-- Jakarta Servlet API -->
        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>6.0.0</version>
            <scope>provided</scope>
        </dependency>

        <!-- Servidor Central (Lógica de Negocios) -->
        <dependency>
            <groupId>com.mycompany</groupId>
            <artifactId>proyecto_pda_grupo02</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>"""

content = re.sub(r'<dependencies>.*?</dependencies>n?', new_deps, content, flags=re.DOTALL)
content = re.sub(r'n\s*<!-- Jakarta EE API -->', '\n        <!-- Jakarta EE API -->', content)
content = re.sub(r'n\s*<dependencies>', '\n    <dependencies>', content)

with open('PDA_WebServer/pom.xml', 'w') as f:
    f.write(content)
