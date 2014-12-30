package de.null_pointer.gui;

import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;

public class HandleValues extends Thread{

	private int[] lsaraw = new int[8];
	private int[] lsaraw_old = new int[8];
	private int distnxraw;
	private int distnxraw_old;
	private int absimuacg_heading;
	private int absimuacg_heading_old;
	private int absimuacg_angle;
	private int absimuacg_angle_old;

	private JFDisplayValues valueGUI = new JFDisplayValues();
	private LSAProcessingPi lsaprocclass = null;
	private Abs_ImuProcessingPi absimuprocclass = null;
	private EOPDProcessingPi eopdLeftprocclass = null;
	private EOPDProcessingPi eopdRightprocclass = null;
	private DistNxProcessingPi distnxprocclass = null;

	public HandleValues(LSAProcessingPi lsaprocclass,
			Abs_ImuProcessingPi absimuprocclass,
			EOPDProcessingPi eopdLeftprocclass,
			EOPDProcessingPi eopdRightprocclass,
			DistNxProcessingPi distnxprocclass) {
		this.lsaprocclass = lsaprocclass;
		this.absimuprocclass = absimuprocclass;
		this.eopdLeftprocclass = eopdLeftprocclass;
		this.eopdRightprocclass = eopdRightprocclass;
		this.distnxprocclass = distnxprocclass;
	}

	public void run() {

		while (true) {

			readLSARaw();
			readDistNXRaw();
			readRightEOPD();
			readLeftEOPD();
			readAbs_ImuCompass_Angle();
			readAbs_ImuCompass_Heading();
			valueGUI.startGUI();

		}

	}

	private void readLSARaw() {

		lsaraw_old = lsaraw;
		lsaraw = lsaprocclass.getLSA();

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

		valueGUI.showRightEOPD(eopdRightprocclass.getDistance());
		

	}

	private void readLeftEOPD() {

		valueGUI.showLeftEOPD(eopdLeftprocclass.getDistance());

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
		absimuacg_angle = absimuprocclass.getAngleHorizontal();
		if (absimuacg_angle != absimuacg_angle_old) {

			valueGUI.showAbsoluteIMUACG_compass_angle(absimuprocclass
					.getAngleHorizontal());

		}
	}

}
