import React from 'react';
import { Layout as LayoutAnt } from 'antd';
import Sidebar from '../Sidebar/Sidebar';
import Header from '../Header/Header';

const { Content, Sider } = LayoutAnt;

const Layout = ({ title, children }) => {
	return (
		<LayoutAnt>
			<Header title={title} />
			<LayoutAnt>
				<Sider width={250} className='site-layout-background'>
					<Sidebar />
				</Sider>
				<LayoutAnt>
					<Content className='site-layout-background content-layout-background'>
						{children}
					</Content>
				</LayoutAnt>
			</LayoutAnt>
		</LayoutAnt>
	);
};

export default Layout;
