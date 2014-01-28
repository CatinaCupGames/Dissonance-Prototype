 #version 120
 //Texture vars
 uniform sampler2D texture;

 //Lighting vars
 uniform vec4 lights[255];
 uniform vec3 colors[255];
 uniform int count;
 uniform float overall_brightness;

 //Screen vars
 uniform vec2 cameraPos;
 uniform vec2 window;
 uniform vec2 aspect;
 uniform vec2 iResolution;

 void main() {
     //Get the original pixel color
     gl_FragColor = texture2D(texture, gl_TexCoord[0].xy);
     //Get the pixel location
     vec2 pos = gl_FragCoord.xy / iResolution.xy;

     //Create constants for scaling
     float scalex = 1.0 / (iResolution.x * 0.5);
     float scaley = 1.0 / (iResolution.y * 0.5);
     float ysomething = window.y / iResolution.y;
     scalex *= window.x / iResolution.x;
     scaley *= ysomething;

     for (int i = 0; i < count; i++) {

         //Translate light's world space to screen space
         vec2 lightPos = vec2(scalex * (lights[i].x - cameraPos.x), ysomething - (scaley * (lights[i].y - cameraPos.y)));

         //Adjust for aspect ratio
         vec2 dis = vec2(abs(pos.x - lightPos.x), abs(pos.y - lightPos.y));
         dis *= (aspect * 0.1);

         //Calculate percent of light
         float xdis = dis.x;
         float ydis = dis.y;
         float magnitude = sqrt((xdis * xdis) + (ydis * ydis));
         float percent = max((lights[i].w - magnitude) / lights[i].w, 0.0);
         if (colors[i].rgb != vec3(0, 0, 0)) {
             gl_FragColor.rgb = (gl_FragColor.rgb * (1.0 - percent)) + (colors[i].rgb * percent);
         }
         gl_FragColor.rgb *= (overall_brightness * (1.0 - percent)) + (lights[i].z * percent);
     }
 }