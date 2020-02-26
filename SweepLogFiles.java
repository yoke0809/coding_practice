package greenways;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ログをスイープするクラス。
 * @author user 村山
 */
public class SweepLogFiles {
	public static final String DIR = "C:\\test\\";				// 指定ディレクトリ
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");	// 日付書式
		String currentDate = sdf.format(new Date());				// 現在日

		// フォルダ内のファイル取得
		File files[] = new File(DIR).listFiles();

		for (File file: files) {
			String updatedDate = sdf.format(file.lastModified());	//更新日
			long dayDiff = calculateDateDiff(sdf, currentDate, updatedDate);

			// 差分日数が10日以上である場合は、指定のディレクトリに移動する
			if (dayDiff >= 10) {
				sweepLogs(file);
				System.out.println(file.getName()+"は、10日以上更新されていない為移動します。");
			}
		}
	}

	/**
	 * 10日以上更新がされていないファイルを指定のディレクトリに移動する。
	 * @param file 指定ディレクトリ配下のファイル
	 */
	private static void sweepLogs(File file) {
		// ファイル名の年月日を抽出する
		String fileName     = file.getName();
		String fileNameY   = fileName.substring(0,4);
		String fileNameYM  = fileName.substring(0,6);
		String fileNameYMD = fileName.substring(0,8);

		// 年月日でディレクトリを作成し階層化する
		File newDirY   = new File(DIR + fileNameY);
		File newDirYM  = new File(newDirY + "\\" + fileNameYM);
		File newDirYMD = new File(newDirYM + "\\" + fileNameYMD);
		newDirY.mkdir();
		newDirYM.mkdir();
		newDirYMD.mkdir();

		// 10日間以上経過したファイルはバックアップフォルダに移動する
		file.renameTo(new File(newDirYMD + "\\" + fileName));
	}

	/**
	 * 現在日と更新日の差分日数を計算し返却する。
	 * @param sdf 日付書式
	 * @param currentDate 現在日
	 * @param updatedDate 更新日
	 * @return 現在日と更新日の差分日数
	 */
	private static long calculateDateDiff(SimpleDateFormat sdf, String currentDate, String updatedDate) {
		long dayDiff = 0;
		try {
			Date parsedCurrentDate = sdf.parse(currentDate);
			Date parsedUpdatedDate = sdf.parse(updatedDate);
			dayDiff = (parsedCurrentDate.getTime() - parsedUpdatedDate.getTime()) / (1000 * 60 * 60 * 24);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dayDiff;
	}
}
