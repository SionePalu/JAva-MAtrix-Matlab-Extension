# JAva-MAtrix-Matlab-Extension
Extension of the original JAMA (JAva MAtrix) package from NIST &amp; MathWorks to include Matlab functions that were missing from JAMA. I've used this JAMA extension over the years for various projects and it's still growing.

This project is based on the original JAMA (https://math.nist.gov/javanumerics/jama/) which was first written in 1998 by MathWorks (developer of Matlab) and NIST. The original JAMA package had 5 classes only, but this JAMA Matlab-like extension, has 500+ files. The package is Matlab-like, meaning that Matlab codes can be ported into Java but with less headaches. There are other packages that are available in this JAMA-extension which were ported from their original Matlab packages.

The original JAMA does not have any "Complex Number" classes, but a similar package "jampackx" is also bundled here so computing with Complex Numbers is available.

There's another similar project here on Github, the LAML, which is a pure Java library for linear algebra and machine learning (https://github.com/SionePalu/LAML) that is also forked from the authors' page to this repository. The JAMA extension will be rewritten in LAML & Apache Spark Machine Learning (https://spark.apache.org/mllib/) over time. This means that the JAMA extension will be rewritten in LAML and Spark, so one can deal with distributed computation for large datasets. Let's be clear here. The Spark Machine Learning API still lacks machine-learning sophistications. It's not lacking small time. It's lacking big time. One can only find the standard machine learning algorithms in Spark. Anything more sophisticated, it's missing. For example, topics in Multi-task, Multi-view, Subspace, Domain-Adaptation learning and so forth, will be available.

