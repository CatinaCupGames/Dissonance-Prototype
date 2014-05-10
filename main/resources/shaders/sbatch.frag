uniform sampler2D texture;

varying vec4 varyColor;
varying vec2 varyTexcoord;

void main() {
    vec4 texColor = texture2D(texture, varyTexcoord);
    gl_FragColor = varyColor * texColor;
}