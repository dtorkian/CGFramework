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
out vec3 specularReflection;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;
uniform vec3 ligthPosition;
uniform float shine;
uniform int ambiente;
uniform int diffuse;
uniform int specular;

struct lightSource
{
  vec4 position;
  vec4 diffuse;
  vec4 specular;
};
lightSource light = lightSource(
    vec4(ligthPosition.xyz, 0.0),
    vec4(0.5, 0.5, 0.5, 1.0),
    vec4(1.0, 1.0, 1.0, 1.0)
);

struct material
{
  vec4 diffuse;
  vec4 specular;
 
};

material mymaterial = material(vec4(1.0, 1.0, 1.0, 1.0),vec4(0.2, 0.2, 0.2, 1.0));

	/**
	 *      Press P   to change between shine = 32 and shine = 132
	 *      Press +   to add 1 to shine
	 *		Press -	  to subtract 1 from shine
	 *		Press 0   to normalize shine
	 *		Press 1   to turn ambient light off/on
	 *		Press 2	  to turn diffuse light off/on
	 *		Press 3	  to turn specular light off/on
	 *		Press Shift+A  or Shift+D to move x-Coordinate of light source
	 *		Press Shift+W  or Shift+S to move y-Coordinate of light source
	 *		Press Shift+Q  or Shift+E to move z-Coordinate of light source
	 */

void main(void) 
{
	if (diffuse==0) light.diffuse = vec4(0);
	if (specular==0) light.specular = vec4(0);
    vNormal     = aNormal;
	gl_Position = uProjection * uView * uModel * vec4(aPosition, 1.0);
	vec3 normalDirection = normalize(vNormal);
    vec3 lightDirection = normalize(vec3(light.position));
    vec3 viewDirection = normalize((uView * uModel * vec4(aPosition, 1.0)).xyz);
    diffuseReflection = normalize(vec3(light.diffuse) * vec3(mymaterial.diffuse)) * max(0.0, dot(normalDirection, lightDirection));
	specularReflection =  vec3(light.specular) * vec3(mymaterial.specular) * pow(max(0.0, dot(reflect(lightDirection,normalDirection),viewDirection)),shine);
	if (ambiente==0) vNormal = vec3(0);
}