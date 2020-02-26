package greenways;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ログをスイープするクラス。
 * @author user 村山
 */
public class SweepLogFiles {
	public static final String DIR = "C:\\test\\";				// 指定ディレクトリ
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");	// 日付書式
	public static Date inputDate;	// 入力日付
	public static Date updatedDate;	// 更新日付

	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.println("半角文字8桁で日付を入力して下さい");
			String inputDateStr = reader.readLine();
			inputDate = setDate(inputDateStr);
		} catch (ParseException e) {
			System.out.println("指定日付のParseExceptionが発生しました");
		} catch (IOException e) {
			System.out.println("IOExceptionが発生しました");
		}

		// フォルダ内のファイル取得
		File files[] = new File(DIR).listFiles();

		for (File file: files) {
			String updatedDateStr = sdf.format(file.lastModified());
			try {
				updatedDate = sdf.parse(updatedDateStr);
			} catch (ParseException e) {
				System.out.println("更新日付のParseExceptionが発生しました");
			}
			long dayDiff = calculateDateDiff(inputDate, updatedDate);

			// 差分日数が10日以上である場合は、指定のディレクトリに移動する
			if (dayDiff >= 10) {
				sweepLogs(file);
				System.out.println(file.getName()+"は、10日以上更新されていない為移動します。");
			}
		}
	}

	private static Date setDate(String date) throws ParseException {
			return sdf.parse(date);
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
	 * @param inputDate 現在日
	 * @param updatedDate 更新日
	 * @return 現在日と更新日の差分日数
	 */
	private static long calculateDateDiff(Date date1, Date date2) {
		long dayDiff = 0;

		long inputDateLong = date1.getTime();
		long updatedDateLong = date2.getTime();

		dayDiff = (inputDateLong - updatedDateLong) / (1000 * 60 * 60 * 24);
		System.out.println("差分日数は"+dayDiff);

		return dayDiff;
	}
}
