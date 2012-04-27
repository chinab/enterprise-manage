/*     */ package com.idb.segmentation;
/*     */ 
/*     */ public class Lexeme
/*     */   implements Comparable<Lexeme>
/*     */ {
/*     */   public static final int TYPE_UNKNOWN = 0;
/*     */   public static final int TYPE_ENGLISH = 1;
/*     */   public static final int TYPE_ARABIC = 2;
/*     */   public static final int TYPE_LETTER = 3;
/*     */   public static final int TYPE_CNWORD = 4;
/*     */   public static final int TYPE_OTHER_CJK = 8;
/*     */   public static final int TYPE_CNUM = 16;
/*     */   public static final int TYPE_COUNT = 32;
/*     */   public static final int TYPE_CQUAN = 48;
/*     */   private int offset;
/*     */   private int begin;
/*     */   private int length;
/*     */   private String lexemeText;
/*     */   private int lexemeType;
/*     */ 
/*     */   public Lexeme(int offset, int begin, int length, int lexemeType)
/*     */   {
/*  64 */     this.offset = offset;
/*  65 */     this.begin = begin;
/*  66 */     if (length < 0) {
/*  67 */       throw new IllegalArgumentException("length < 0");
/*     */     }
/*  69 */     this.length = length;
/*  70 */     this.lexemeType = lexemeType;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/*  79 */     if (o == null) {
/*  80 */       return false;
/*     */     }
/*     */ 
/*  83 */     if (this == o) {
/*  84 */       return true;
/*     */     }
/*     */ 
/*  87 */     if ((o instanceof Lexeme)) {
/*  88 */       Lexeme other = (Lexeme)o;
/*     */ 
/*  92 */       return (this.offset == other.getOffset()) && 
/*  90 */         (this.begin == other.getBegin()) && 
/*  91 */         (this.length == other.getLength());
/*     */     }
/*     */ 
/*  97 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 106 */     int absBegin = getBeginPosition();
/* 107 */     int absEnd = getEndPosition();
/* 108 */     return absBegin * 37 + absEnd * 31 + absBegin * absEnd % getLength() * 11;
/*     */   }
/*     */ 
/*     */   public int compareTo(Lexeme other)
/*     */   {
/* 117 */     if (this.begin < other.getBegin())
/* 118 */       return -1;
/* 119 */     if (this.begin == other.getBegin())
/*     */     {
/* 121 */       if (this.length > other.getLength())
/* 122 */         return -1;
/* 123 */       if (this.length == other.getLength()) {
/* 124 */         return 0;
/*     */       }
/* 126 */       return 1;
/*     */     }
/*     */ 
/* 130 */     return 1;
/*     */   }
/*     */ 
/*     */   public int getOffset()
/*     */   {
/* 135 */     return this.offset;
/*     */   }
/*     */ 
/*     */   public void setOffset(int offset) {
/* 139 */     this.offset = offset;
/*     */   }
/*     */ 
/*     */   public int getBegin() {
/* 143 */     return this.begin;
/*     */   }
/*     */ 
/*     */   public int getBeginPosition()
/*     */   {
/* 150 */     return this.offset + this.begin;
/*     */   }
/*     */ 
/*     */   public void setBegin(int begin) {
/* 154 */     this.begin = begin;
/*     */   }
/*     */ 
/*     */   public int getEndPosition()
/*     */   {
/* 162 */     return this.offset + this.begin + this.length;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 170 */     return this.length;
/*     */   }
/*     */ 
/*     */   public void setLength(int length) {
/* 174 */     if (this.length < 0) {
/* 175 */       throw new IllegalArgumentException("length < 0");
/*     */     }
/* 177 */     this.length = length;
/*     */   }
/*     */ 
/*     */   public String getLexemeText()
/*     */   {
/* 185 */     if (this.lexemeText == null) {
/* 186 */       return "";
/*     */     }
/* 188 */     return this.lexemeText;
/*     */   }
/*     */ 
/*     */   public void setLexemeText(String lexemeText) {
/* 192 */     if (lexemeText == null) {
/* 193 */       this.lexemeText = "";
/* 194 */       this.length = 0;
/*     */     } else {
/* 196 */       this.lexemeText = lexemeText;
/* 197 */       this.length = lexemeText.length();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getLexemeType()
/*     */   {
/* 206 */     return this.lexemeType;
/*     */   }
/*     */ 
/*     */   public void setLexemeType(int lexemeType) {
/* 210 */     this.lexemeType = lexemeType;
/*     */   }
/*     */ 
/*     */   public boolean append(Lexeme l, int lexemeType)
/*     */   {
/* 220 */     if ((l != null) && (getEndPosition() == l.getBeginPosition())) {
/* 221 */       this.length += l.getLength();
/* 222 */       this.lexemeType = lexemeType;
/* 223 */       return true;
/*     */     }
/* 225 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 233 */     StringBuffer strbuf = new StringBuffer();
/* 234 */     strbuf.append(getBeginPosition()).append("-").append(getEndPosition());
/* 235 */     strbuf.append(" : ").append(this.lexemeText).append(" : \t");
/* 236 */     switch (this.lexemeType) {
/*     */     case 0:
/* 238 */       strbuf.append("UNKONW");
/* 239 */       break;
/*     */     case 1:
/* 241 */       strbuf.append("ENGLISH");
/* 242 */       break;
/*     */     case 2:
/* 244 */       strbuf.append("ARABIC");
/* 245 */       break;
/*     */     case 3:
/* 247 */       strbuf.append("LETTER");
/* 248 */       break;
/*     */     case 4:
/* 250 */       strbuf.append("CN_WORD");
/* 251 */       break;
/*     */     case 8:
/* 253 */       strbuf.append("OTHER_CJK");
/* 254 */       break;
/*     */     case 32:
/* 256 */       strbuf.append("COUNT");
/* 257 */       break;
/*     */     case 16:
/* 259 */       strbuf.append("CN_NUM");
/* 260 */       break;
/*     */     case 48:
/* 262 */       strbuf.append("CN_QUAN");
/*     */     }
/*     */ 
/* 266 */     return strbuf.toString();
/*     */   }
/*     */ }

/* Location:           D:\TDDOWNLOAD\IKAnalyzer2012_u4\
 * Qualified Name:     org.wltea.analyzer.core.Lexeme
 * JD-Core Version:    0.6.0
 */