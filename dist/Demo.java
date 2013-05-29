
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * 标注的分词方式,这里面的流你可以传入任何流.除了流氓
 * @author ansj
 *
 */
public class Demo {
	public static void main(String[] args) {
		List<Term> paser = BaseAnalysis.paser("若雅虎关闭了,我就不访问网站了") ;
		System.out.println(paser);
	}
}
