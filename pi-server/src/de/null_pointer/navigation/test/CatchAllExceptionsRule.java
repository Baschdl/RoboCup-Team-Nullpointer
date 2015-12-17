package de.null_pointer.navigation.test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * This rule catches exceptions on all threads and fails the test if such
 * exceptions are caught
 * 
 * @author Johannes Schneider (<a
 *         href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CatchAllExceptionsRule implements TestRule {
	@Nullable
	private Thread.UncaughtExceptionHandler oldHandler;

	@Override
	public Statement apply(final Statement base, Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				before();
				try {
					base.evaluate();
				} catch (Throwable t) {
					afterFailing();
					throw t;
				}

				afterSuccess();
			}
		};
	}

	private void before() {
		oldHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				caught.add(e);
				if (oldHandler != null) {
					oldHandler.uncaughtException(t, e);
				}
			}
		});
	}

	@NonNull
	private final List<Throwable> caught = new ArrayList<Throwable>();

	private void afterSuccess() {
		Thread.setDefaultUncaughtExceptionHandler(oldHandler);

		if (caught.isEmpty()) {
			return;
		}

		throw new AssertionError(buildMessage());
	}

	private String buildMessage() {
		StringBuilder builder = new StringBuilder();
		builder.append(caught.size()).append(
				" exceptions thrown but not caught in other threads:\n");

		for (Throwable throwable : caught) {
			throwable.printStackTrace();
			builder.append("---------------------\n");

			StringWriter out = new StringWriter();
			throwable.printStackTrace(new PrintWriter(out));
			builder.append(out.toString());
		}

		builder.append("---------------------\n");

		return builder.toString();
	}

	private void afterFailing() {
		Thread.setDefaultUncaughtExceptionHandler(oldHandler);
	}
}