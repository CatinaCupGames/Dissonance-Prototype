varying vec4 texcoord;

uniform sampler2D texture;

uniform vec4 color;
uniform float intensity;

void main() {

//	gl_FragColor = texture2D(texture, texcoord.xy);
	if(texture2D(texture,texcoord.xy).a < 0.5){	//ugh conditionals, but it works as a test
		vec3 add;
		add = textureOffset(texture,texcoord.xy, ivec2(1.0, 1.0)).rgb; // probably should do texturegather/offset
		add += textureOffset(texture,texcoord.xy, ivec2(1.0, -1.0)).rgb; // probably should do texturegather/offset
		add += textureOffset(texture,texcoord.xy, ivec2(-1.0, -1.0)).rgb; // probably should do texturegather/offset
		add += textureOffset(texture,texcoord.xy, ivec2(-1.0, 1.0)).rgb; // probably should do texturegather/offset

		add += textureOffset(texture,texcoord.xy, ivec2(0.0, -1.0)).rgb; // probably should do texturegather/offset
		add += textureOffset(texture,texcoord.xy, ivec2(0.0, 1.0)).rgb; // probably should do texturegather/offset
		add += textureOffset(texture,texcoord.xy, ivec2(1.0, 0.0)).rgb; // probably should do texturegather/offset
		add += textureOffset(texture,texcoord.xy, ivec2(-1.0, 0.0)).rgb; // probably should do texturegather/offset

		gl_FragColor.rgb = add * 0.1;
//		gl_FragColor.a = (add.r + add.g + add.b) * 0.05;
	}

}
