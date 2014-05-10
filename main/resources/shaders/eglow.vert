varying vec4 texcoord;

void main(void)
{
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    texcoord = gl_MultiTexCoord0;
    gl_FrontColor = gl_Color;
    gl_BackColor = gl_Color;
}
