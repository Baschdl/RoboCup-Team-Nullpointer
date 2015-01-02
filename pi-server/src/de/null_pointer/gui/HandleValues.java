package de.null_pointer.gui;

import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;

public class HandleValues extends Thread {

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

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			readLSARaw();
			readDistNXRaw();
			readRightEOPD();
			readLeftEOPD();
			readAbs_ImuCompass_Angle();
			readAbs_ImuCompass_Heading();

		}

	}

	private void readLSARaw() {

		valueGUI.showLSARaw(lsaprocclass.getLSA());

	}

	private void readDistNXRaw() {

			valueGUI.showDistNXRaw(distnxprocclass.getDistance());

	}

	private void readRightEOPD() {

		valueGUI.showRightEOPD(eopdRightprocclass.getDistance());

	}

	private void readLeftEOPD() {

		valueGUI.showLeftEOPD(eopdLeftprocclass.getDistance());

	}

	private void readAbs_ImuCompass_Heading() {

		valueGUI.showAbsoluteIMUACG_compass_heading(absimuprocclass.getHeading());
	}

	private void readAbs_ImuCompass_Angle() {

		valueGUI.showAbsoluteIMUACG_compass_angle(absimuprocclass
				.getAngleHorizontal());

	}

}
