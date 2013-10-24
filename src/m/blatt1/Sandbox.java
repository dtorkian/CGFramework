package m.blatt1;

/* 
 * Cologne University of Applied Sciences
 * Institute for Media and Imaging Technologies - Computer Graphics Group
 *
 * Copyright (c) 2012 Cologne University of Applied Sciences. All rights reserved.
 *
 * This source code is property of the Cologne University of Applied Sciences. Any redistribution
 * and use in source and binary forms, with or without modification, requires explicit permission. 
 */

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.*;
import math.Mat4;
import math.MathUtil;
import math.Vec3;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import util.Color;
import util.Mesh;
import util.ShaderProgram;

public class Sandbox {
	private ShaderProgram shaderProgram;
	private Mesh mesh;
	private Mat4 modelMatrix;
	private Mat4 projectionMatrix;

	public float red = 0;
	public float green = 0;
	public float blue = 1;

	public Sandbox() {
		shaderProgram = new ShaderProgram("Color_vs.glsl", "Color_fs.glsl");

		float[] positions = { -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f, 0.0f,
				0.5f, 0.0f };

		int[] indices = { 0, 1, 2 };

		mesh = new Mesh(GL_STATIC_DRAW);
		mesh.setAttribute(0, positions, 3);
		mesh.setIndices(indices);

		modelMatrix = new Mat4();
		projectionMatrix = Mat4.perspective(60, 16.0f, 9.0f, 0.01f, 10.0f);
		projectionMatrix.mul(Mat4.translation(0.0f, 0.0f, -2.0f));

		Vec3[] trianglePoints = new Vec3[3];
		for (int i = 0; i < indices.length; i++) {
			trianglePoints[i] = new Vec3(positions[0 + i * 3],
					positions[1 + i * 3], positions[2 + i * 3]);
		}

		Vec3 aSubC = trianglePoints[0].sub(trianglePoints[2]);
		Vec3 bSubC = trianglePoints[1].sub(trianglePoints[2]);

		Vec3 crossProduct = Vec3.cross(aSubC, bSubC);
		System.out.println("a) Die Normale lautet : x=" + crossProduct.x
				+ ", y=" + crossProduct.y + ", z=" + crossProduct.z + ".");

		Vec3 a = trianglePoints[0];
		Vec3 b = trianglePoints[1];
		Vec3 c = trianglePoints[2];

		float xa = Vec3.dot(Vec3.sub(c, a), Vec3.sub(b, a));
		float ya = Vec3.length(Vec3.sub(c, a)) * Vec3.length(Vec3.sub(b, a));

		float xb = Vec3.dot(Vec3.sub(a, c), Vec3.sub(b, c));
		float yb = Vec3.length(Vec3.sub(a, c)) * Vec3.length(Vec3.sub(b, c));

		float xc = Vec3.dot(Vec3.sub(c, b), Vec3.sub(a, b));
		float yc = Vec3.length(Vec3.sub(c, b)) * Vec3.length(Vec3.sub(a, b));

		System.out.println("b) " + MathUtil.toDeg((float) Math.acos(xa / ya))
				+ " " + MathUtil.toDeg((float) Math.acos(xb / yb)) + " "
				+ MathUtil.toDeg((float) Math.acos(xc / yc)));

		a = Vec3.sub(trianglePoints[1], trianglePoints[0]);
		b = Vec3.sub(trianglePoints[2], trianglePoints[0]);
		c = Vec3.sub(trianglePoints[2], trianglePoints[1]);

		System.out.println("c) " + Vec3.length(a) + " " + Vec3.length(b) + " "
				+ Vec3.length(c));
	}

	/**
	 * @param deltaTime
	 *            The time in seconds between the last two frames
	 */
	int counter = 0;

	public void update(float deltaTime) {
		counter++;
		if (counter <= 100)
			red += 0.01;
		green += 0.01;
		blue -= 0.01;
		if (counter > 100)
			red -= 0.01;
		green -= 0.01;
		blue += 0.01;
		if (counter == 200)
			counter = 0;

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			Main.exit = true;

		if (Mouse.isButtonDown(0)) {
			float rotationScale = 0.01f;

			float deltaX = (float) Mouse.getDX();
			float deltaY = (float) Mouse.getDY();

			Mat4 rotationX = Mat4
					.rotation(Vec3.yAxis(), deltaX * rotationScale);
			Mat4 rotationY = Mat4.rotation(Vec3.xAxis(), -deltaY
					* rotationScale);

			modelMatrix = rotationY.mul(rotationX).mul(modelMatrix);
		}
	}

	public void draw() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glClearColor(red % 1, green % 1, blue % 1, (float) Math.random());
		// System.out.println(red);

		shaderProgram.useProgram();
		shaderProgram.setUniform("uColor", new Vec3(1 - (red % 1), (red % 1),
				1 - (red % 1)));
		shaderProgram.setUniform("uModel", modelMatrix);
		shaderProgram.setUniform("uProjection", projectionMatrix);

		mesh.draw(GL_TRIANGLES);
	}
}
