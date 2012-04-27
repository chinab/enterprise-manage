/*     */package com.idb.segmentation;

/*     */
/*     */import java.io.IOException; /*     */
import java.io.Reader;
import java.io.StringReader; /*     */
import java.util.ArrayList; /*     */
import java.util.List;

import com.idb.segmentation.cfg.Configuration;
import com.idb.segmentation.dic.Dictionary;

/*     */
/*     */public final class CNSegmentation
/*     */{
	/*     */private Reader input;
	/*     */private Configuration cfg;
	/*     */private AnalyzeContext context;
	/*     */private List<ISegmenter> segmenters;
	/*     */private IDBArbitrator arbitrator;

	/*     */
	/*     */public CNSegmentation(Reader input, boolean useSmart)
	/*     */{
		/* 61 */this.input = input;
		/* 62 */this.cfg = Configuration.getInstance();
		/* 63 */this.cfg.setUseSmart(useSmart);
		/* 64 */init();
		/*     */}

	/*     */
	/*     */public CNSegmentation(Reader input, Configuration cfg)
	/*     */{
		/* 74 */this.input = input;
		/* 75 */this.cfg = cfg;
		/* 76 */init();
		/*     */}

	/*     */
	/*     */private void init()
	/*     */{
		/* 84 */Dictionary.initial(this.cfg);
		/*     */
		/* 86 */this.context = new AnalyzeContext(this.cfg);
		/*     */
		/* 88 */this.segmenters = loadSegmenters();
		/*     */
		/* 90 */this.arbitrator = new IDBArbitrator();
		/*     */}

	/*     */
	/*     */private List<ISegmenter> loadSegmenters()
	/*     */{
		/* 98 */List segmenters = new ArrayList(4);
		/*     */
		/* 100 */segmenters.add(new LetterSegmenter());
		/*     */
		/* 102 */segmenters.add(new CN_QuantifierSegmenter());
		/*     */
		/* 104 */segmenters.add(new CJKSegmenter());
		/* 105 */return segmenters;
		/*     */}

	/*     */
	/*     */public synchronized Lexeme next()
	/*     */throws IOException
	/*     */{
		/* 114 */if (this.context.hasNextResult())
		/*     */{
			/* 116 */return this.context.getNextLexeme();
			/*     */}
		/*     */
		/* 123 */int available = this.context.fillBuffer(this.input);
		/* 124 */if (available <= 0)
		/*     */{
			/* 126 */this.context.reset();
			/* 127 */return null;
			/*     */}
		/*     */
		/* 131 */this.context.initCursor();
		/*     */do
		/*     */{
			/* 134 */for (ISegmenter segmenter : this.segmenters) {
				/* 135 */segmenter.analyze(this.context);
				/*     */}
			/*     */}
		/* 138 */while ((!this.context.needRefillBuffer()) && (
		/* 142 */this.context.moveCursor()));
		/*     */
		/* 144 */for (ISegmenter segmenter : this.segmenters) {
			/* 145 */segmenter.reset();
			/*     */}
		/*     */
		/* 149 */this.arbitrator.process(this.context, this.cfg.useSmart());
		/*     */
		/* 151 */this.context.processUnkownCJKChar();
		/*     */
		/* 153 */this.context.markBufferOffset();
		/*     */
		/* 155 */if (this.context.hasNextResult()) {
			/* 156 */return this.context.getNextLexeme();
			/*     */}
		/* 158 */return null;
		/*     */}

	/*     */
	/*     */public synchronized void reset(Reader input)
	/*     */{
		/* 167 */this.input = input;
		/* 168 */this.context.reset();
		/* 169 */for (ISegmenter segmenter : this.segmenters)
			/* 170 */segmenter.reset();
		/*     */}

	public static void main(String[] args) {
		Reader input = new StringReader("相信史诗装备国利害人");
		CNSegmentation cnSegmentation = new CNSegmentation(input, true);
		Lexeme next = null;
		do {
			try {
				next = cnSegmentation.next();
				if (next != null) {
					System.out.println(next.getLexemeText());
				}
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}

		} while (next != null);
	}
	/*     */
}

/*
 * Location: D:\TDDOWNLOAD\IKAnalyzer2012_u4\ Qualified Name:
 * org.wltea.analyzer.core.IKSegmenter JD-Core Version: 0.6.0
 */