package de.null_pointer.gui;

import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;

public class HandleValues {

	private int[] lsaraw = new int[8];
	private int[] lsaraw_old = new int[8];
	private int distnxraw;
	private int distnxraw_old;
	private int absimuacg_heading;
	private int absimuacg_heading_old;
	private int absimuacg_angle;
	private int absimuacg_angle_old;

	private JFDisplayValues valueGUI = new JFDisplayValues();
	private LSAProcessingPi lsaprocclass = new LSAProcessingPi();
	private Abs_ImuProcessingPi absimuprocclass = new Abs_ImuProcessingPi();
	private EOPDProcessingPi eopdprocclass = new EOPDProcessingPi();
	private DistNxProcessingPi distnxprocclass = new DistNxProcessingPi();

	public void readValues() {

		while (true) {

			readLSARaw();
			readDistNXRaw();
			readRightEOPD();
			readLeftEOPD();
			readAbs_ImuCompass_Angle();
			readAbs_ImuCompass_Heading();
			
		}

	}

	private void readLSARaw() {

		lsaraw_old = lsaraw.clone();
		lsaraw = lsaprocclass.getLSA().clone();

		if (lsaraw[0] != lsaraw_old[0] || lsaraw[1] != lsaraw_old[1]
				|| lsaraw[2] != lsaraw_old[2] || lsaraw[3] != lsaraw_old[3]
				|| lsaraw[4] != lsaraw_old[4] || lsaraw[5] != lsaraw_old[5]
				|| lsaraw[6] != lsaraw_old[6] || lsaraw[7] != lsaraw_old[7]) {

			valueGUI.showLSARaw(lsaraw);

		}

	}

	private void readDistNXRaw() {
		distnxraw_old = distnxraw;
		distnxraw = distnxprocclass.getDistance();

		if (distnxraw != distnxraw_old) {

			valueGUI.showDistNXRaw(distnxraw);

		}
	}

	private void readRightEOPD() {

		// valueGUI.showLeftEOPD(eopdprocclass.getRightEOPD());
		// TODO EOPD-Klasse fertigstellen

	}

	private void readLeftEOPD() {

		// valueGUI.showRightEOPD(eopdprocclass.getLeftEOPD());

	}

	private void readAbs_ImuCompass_Heading() {
		absimuacg_heading_old = absimuacg_heading;
		absimuacg_heading = absimuprocclass.getHeading();
		if (absimuacg_heading != absimuacg_heading_old) {
			valueGUI.showAbsoluteIMUACG_compass_heading(absimuacg_heading);
		}
	}

	private void readAbs_ImuCompass_Angle() {

		absimuacg_angle_old = absimuacg_angle;
		absimuacg_angle = absimuprocclass.getAngle();
		if (absimuacg_angle != absimuacg_angle_old) {

			valueGUI.showAbsoluteIMUACG_compass_angle(absimuprocclass.getAngle());

		}
	}

}
