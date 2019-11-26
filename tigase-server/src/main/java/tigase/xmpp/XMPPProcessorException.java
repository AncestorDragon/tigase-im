/**
 * Tigase XMPP Server - The instant messaging server
 * Copyright (C) 2004 Tigase, Inc. (office@tigase.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. Look for COPYING file in the top folder.
 * If not, see http://www.gnu.org/licenses/.
 */
package tigase.xmpp;

import tigase.server.Packet;

/**
 * Created by bmalkow on 18.04.2017.
 */
public class XMPPProcessorException
		extends XMPPException {

	private static final long serialVersionUID = 1L;

	private Authorization errorCondition;

	private String text;

	public XMPPProcessorException(final Authorization errorCondition) {
		this(errorCondition, (String) null, (String) null);
	}

	/**
	 * @param errorCondition
	 * @param text human readable message will be send to client
	 */
	public XMPPProcessorException(Authorization errorCondition, String text) {
		this(errorCondition, text, (String) null);
	}

	public XMPPProcessorException(Authorization errorCondition, String text, Throwable cause) {
		this(errorCondition, text, (String) null, cause);
	}

	/**
	 * @param errorCondition
	 * @param message exception message for logging
	 * @param text human readable message will be send to client
	 */
	public XMPPProcessorException(Authorization errorCondition, String text, String message) {
		this(errorCondition, text, message, null);
	}

	public XMPPProcessorException(Authorization errorCondition, String text, String message, Throwable cause) {
		super(message, cause);
		this.errorCondition = errorCondition;
		this.text = text;
	}

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return String.valueOf(this.errorCondition.getErrorCode());
	}

	public Authorization getErrorCondition() {
		return errorCondition;
	}

	@Override
	public String getMessage() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getErrorMessagePrefix());
		sb.append(errorCondition.getCondition()).append(" ");
		if (text != null) {
			sb.append("with message: \"").append(text).append("\" ");
		}
		if (super.getMessage() != null) {
			sb.append("(").append(super.getMessage()).append(") ");
		}

		return sb.toString();
	}

	public String getMessageWithPosition() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getMessage());

		StackTraceElement[] stack = getStackTrace();
		if (stack.length > 0) {
			sb.append("generated by ");
			sb.append(getStackTrace()[0].toString());
			sb.append(" ");
		}

		return sb.toString();
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return errorCondition.getCondition();
	}

	public String getText() {
		return text;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return errorCondition.getErrorType();
	}

	public Packet makeElement(Packet packet, boolean insertOriginal) throws PacketErrorTypeException {
		Packet result = errorCondition.getResponseMessage(packet, text, insertOriginal);
		return result;
	}

	protected String getErrorMessagePrefix() {
		return "XMPP error condition: ";
	}
}
