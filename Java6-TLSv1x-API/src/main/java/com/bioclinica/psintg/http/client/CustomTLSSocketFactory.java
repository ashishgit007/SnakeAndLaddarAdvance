package com.bioclinica.psintg.http.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.security.cert.X509Certificate;

import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.DefaultTlsClient;
import org.bouncycastle.crypto.tls.ExtensionType;
import org.bouncycastle.crypto.tls.TlsAuthentication;
import org.bouncycastle.crypto.tls.TlsClientProtocol;
import org.bouncycastle.crypto.tls.TlsCredentials;

/**
 * 
 * @author Ashish.Trivedi Purpose of this class is to extend jre
 *         SSLsocketfactory and Use TLSv1.1,1.2 to complete handshake and crate
 *         socket it might have lots of overridden methods , please ignore them
 *         as they might be of type Abstract without this class jre6 will fail
 *         in SSL HandShake, Bouncy castle cannot have SSl context hence we cant
 *         use Apache HttpClient
 *
 */
public class CustomTLSSocketFactory extends SSLSocketFactory {

	String host;
	int port;
	int readTimeout;

	private SSLSocketFactory internalSSLSocketFactory;

	public CustomTLSSocketFactory(int readTimeout) throws KeyManagementException, NoSuchAlgorithmException {
		SSLContext context = SSLContext.getInstance("TLS");
		this.readTimeout=readTimeout;
		context.init(null, null, null);
		internalSSLSocketFactory = context.getSocketFactory();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return internalSSLSocketFactory.getDefaultCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return internalSSLSocketFactory.getSupportedCipherSuites();
	}

	/**
	 * This is main method which uses Bouncy Castle to Just create HandShake
	 * 
	 * @param host
	 * @param tlsClientProtocol
	 * @return SSL Socket
	 */
	private SSLSocket _createSSLSocket(final String host, final TlsClientProtocol tlsClientProtocol) {
		return new SSLSocket() {
			private java.security.cert.Certificate[] peertCerts;

			@Override
			public InputStream getInputStream() throws IOException {
				return tlsClientProtocol.getInputStream();
			}

			@Override
			public OutputStream getOutputStream() throws IOException {
				return tlsClientProtocol.getOutputStream();
			}

			@Override
			public synchronized void close() throws IOException {
				tlsClientProtocol.close();
			}

			@Override
			public void addHandshakeCompletedListener(HandshakeCompletedListener arg0) {

			}

			@Override
			public boolean getEnableSessionCreation() {
				return false;
			}

			@Override
			public String[] getEnabledCipherSuites() {
				return null;
			}

			@Override
			public String[] getEnabledProtocols() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean getNeedClientAuth() {
				return false;
			}

			@Override
			public SSLSession getSession() {
				return new SSLSession() {

					@Override
					public int getApplicationBufferSize() {
						return 0;
					}

					@Override
					public String getCipherSuite() {
						// throw new UnsupportedOperationException();
						return "";
					}

					@Override
					public long getCreationTime() {
						throw new UnsupportedOperationException();
					}

					@Override
					public byte[] getId() {
						throw new UnsupportedOperationException();
					}

					@Override
					public long getLastAccessedTime() {
						throw new UnsupportedOperationException();
					}

					@Override
					public java.security.cert.Certificate[] getLocalCertificates() {
						throw new UnsupportedOperationException();
					}

					@Override
					public Principal getLocalPrincipal() {
						throw new UnsupportedOperationException();
					}

					@Override
					public int getPacketBufferSize() {
						throw new UnsupportedOperationException();
					}

					@Override
					public X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public java.security.cert.Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
						return peertCerts;
					}

					@Override
					public String getPeerHost() {
						throw new UnsupportedOperationException();
					}

					@Override
					public int getPeerPort() {
						return 0;
					}

					@Override
					public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
						return null;
						// throw new UnsupportedOperationException();

					}

					@Override
					public String getProtocol() {
						throw new UnsupportedOperationException();
					}

					@Override
					public SSLSessionContext getSessionContext() {
						throw new UnsupportedOperationException();
					}

					@Override
					public Object getValue(String arg0) {
						throw new UnsupportedOperationException();
					}

					@Override
					public String[] getValueNames() {
						throw new UnsupportedOperationException();
					}

					@Override
					public void invalidate() {
						// throw new UnsupportedOperationException();

					}

					@Override
					public boolean isValid() {
						throw new UnsupportedOperationException();
					}

					@Override
					public void putValue(String arg0, Object arg1) {
						throw new UnsupportedOperationException();

					}

					@Override
					public void removeValue(String arg0) {
						throw new UnsupportedOperationException();

					}

				};
			}

			@Override
			public String[] getSupportedProtocols() {
				return null;
			}

			@Override
			public boolean getUseClientMode() {
				return false;
			}

			@Override
			public boolean getWantClientAuth() {

				return false;
			}

			@Override
			public void removeHandshakeCompletedListener(HandshakeCompletedListener arg0) {

			}

			@Override
			public void setEnableSessionCreation(boolean arg0) {

			}

			@Override
			public void setEnabledCipherSuites(String[] arg0) {

			}

			@Override
			public void setEnabledProtocols(String[] arg0) {

			}

			@Override
			public void setNeedClientAuth(boolean arg0) {

			}

			@Override
			public void setUseClientMode(boolean arg0) {

			}

			@Override
			public void setWantClientAuth(boolean arg0) {

			}

			@Override
			public String[] getSupportedCipherSuites() {
				return null;
			}

			@Override
			public void startHandshake() throws IOException {
				tlsClientProtocol.connect(new DefaultTlsClient() {
					@Override
					public Hashtable<Integer, byte[]> getClientExtensions() throws IOException {
						Hashtable<Integer, byte[]> clientExtensions = super.getClientExtensions();
						if (clientExtensions == null) {
							clientExtensions = new Hashtable<Integer, byte[]>();
						}

						// Add host_name
						byte[] host_name = host.getBytes();

						final ByteArrayOutputStream baos = new ByteArrayOutputStream();
						final DataOutputStream dos = new DataOutputStream(baos);
						dos.writeShort(host_name.length + 3); // entry size
						dos.writeByte(0); // name type = hostname
						dos.writeShort(host_name.length);
						dos.write(host_name);
						dos.close();
						clientExtensions.put(ExtensionType.server_name, baos.toByteArray());
						return clientExtensions;
					}

					@Override
					public TlsAuthentication getAuthentication() throws IOException {
						return new TlsAuthentication() {

							@Override
							public void notifyServerCertificate(Certificate serverCertificate) throws IOException {

								try {
									CertificateFactory cf = CertificateFactory.getInstance("X.509");
									List<java.security.cert.Certificate> certs = new LinkedList<java.security.cert.Certificate>();
									for (org.bouncycastle.asn1.x509.Certificate c : serverCertificate
											.getCertificateList()) {
										certs.add(cf.generateCertificate(new ByteArrayInputStream(c.getEncoded())));
									}
									peertCerts = certs.toArray(new java.security.cert.Certificate[0]);
								} catch (CertificateException e) {
									System.out.println("Failed to cache server certs" + e);
									throw new IOException(e);
								}

							}

							@Override
							public TlsCredentials getClientCredentials(CertificateRequest arg0) throws IOException {
								return null;
							}

						};

					}

				});

			}

		};// Socket

	}

	@Override
	public Socket createSocket() throws IOException {
		// return internalSSLSocketFactory.createSocket();
        
		Socket socket = new Socket();
		//socket.setSoTimeout(2000);
		//System.out.println("this.readTimeout"+this.readTimeout);
		socket.setSoTimeout(this.readTimeout);
		socket.connect(new InetSocketAddress(host, port));
		SecureRandom _secureRandom = new SecureRandom();
		final TlsClientProtocol tlsClientProtocol = new TlsClientProtocol(socket.getInputStream(),
				socket.getOutputStream(), _secureRandom);
		Socket ss = _createSSLSocket(host, tlsClientProtocol);// .setEnabledProtocols(new
																// String[]{"TLSv1"});;

		return ss;

	}

	public CustomTLSSocketFactory(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	@Override
	public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
		return enableTLSOnSocket(internalSSLSocketFactory.createSocket(s, host, port, autoClose));
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port));
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
			throws IOException, UnknownHostException {
		return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port, localHost, localPort));
	}

	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException {
		return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port));
	}

	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
			throws IOException {
		return enableTLSOnSocket(internalSSLSocketFactory.createSocket(address, port, localAddress, localPort));
	}

	private Socket enableTLSOnSocket(Socket socket) {
		if (socket != null && (socket instanceof SSLSocket)) {
			((SSLSocket) socket).setEnabledProtocols(new String[] { "TLSv1.1","TLSv1.1", "TLSv1.2" });
		}
		return socket;
	}
}