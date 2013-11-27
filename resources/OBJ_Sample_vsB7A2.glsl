/* 
 * Cologne University of Applied Sciences
 * Institute for Media and Imaging Technologies - Computer Graphics Group
 *
 * Copyright (c) 2012 Cologne University of Applied Sciences. All rights reserved.
 *
 * This source code is property of the Cologne University of Applied Sciences. Any redistribution
 * and use in source and binary forms, with or without modification, requires explicit permission. 
 */


#version 150
#extension GL_ARB_explicit_attrib_location : enable


layout(location=0) in vec3 aPosition;
layout(location=1) in vec3 aNormal;

out vec3 vNormal;
out vec3 diffuseReflection;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;
uniform vec3 ligthPosition;

struct lightSource
{
  vec4 position;
  vec4 diffuse;
};
lightSource light0 = lightSource(
    vec4(ligthPosition.xyz, 0.0),
    vec4(1.0, 1.0, 1.0, 1.0)
);

struct material
{
  vec4 diffuse;
};

material mymaterial = material(vec4(1.0, 1.0, 1.0, 1.0));

void main(void) 
{
  
	gl_Position = uProjection * uView * uModel * vec4(aPosition, 1.0);
	vNormal     = aNormal;
	vec3 normalDirection = normalize(vNormal);
    vec3 lightDirection = normalize(vec3(light0.position));
    diffuseReflection = vec3(light0.diffuse) * vec3(mymaterial.diffuse) * max(0.0, dot(normalDirection, lightDirection));
	
}