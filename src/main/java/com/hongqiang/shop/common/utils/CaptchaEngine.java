package com.hongqiang.shop.common.utils;

import com.octo.captcha.component.image.backgroundgenerator.FileReaderRandomBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import java.awt.Color;
import java.awt.Font;
import org.springframework.core.io.ClassPathResource;

public class CaptchaEngine extends ListImageCaptchaEngine {
	private static final int WIDTH = 80;
	private static final int HEIGHT = 28;
	private static final int LEFT = 12;
	private static final int RIGHT = 16;
	private static final int NUMBER = 4;
	private static final int POSITION = 4;
	private static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String CAPTCHA_PATH = "/captcha/";
	private static final Font[] FONT_STRING = { new Font("nyala", 1, 16),
			new Font("Arial", 1, 16), new Font("nyala", 1, 16),
			new Font("Bell", 1, 16), new Font("Bell MT", 1, 16),
			new Font("Credit", 1, 16), new Font("valley", 1, 16),
			new Font("Impact", 1, 16) };
	private static final Color[] COLOR_STRING = { new Color(255, 255, 255),
			new Color(255, 220, 220), new Color(220, 255, 255),
			new Color(220, 220, 255), new Color(255, 255, 220),
			new Color(220, 255, 220) };

	protected void buildInitialFactories() {
		RandomFontGenerator localRandomFontGenerator = new RandomFontGenerator(
				Integer.valueOf(LEFT), Integer.valueOf(RIGHT), FONT_STRING);
		FileReaderRandomBackgroundGenerator localFileReaderRandomBackgroundGenerator = new FileReaderRandomBackgroundGenerator(
				Integer.valueOf(WIDTH), Integer.valueOf(HEIGHT),
				new ClassPathResource(CAPTCHA_PATH).getPath());
		DecoratedRandomTextPaster localDecoratedRandomTextPaster = new DecoratedRandomTextPaster(
				Integer.valueOf(NUMBER), Integer.valueOf(POSITION),
				new RandomListColorGenerator(COLOR_STRING),
				new TextDecorator[0]);
		addFactory(new GimpyFactory(new RandomWordGenerator(ALPHA),
				new ComposedWordToImage(localRandomFontGenerator,
						localFileReaderRandomBackgroundGenerator,
						localDecoratedRandomTextPaster)));
	}
}