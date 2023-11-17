import React, { useState } from 'react';
import { CaretDownOutlined, CaretUpOutlined } from '@ant-design/icons';

const CommonHeaderSort = ({ title, dataIndex, sortBy, onSort }) => {
	const [sortDir, setSortDir] = useState('asc');

	const onHandleSort = () => {
		const newSortDir = sortDir === 'asc' ? 'desc' : 'asc';
		onSort(dataIndex, newSortDir);
		setSortDir(newSortDir);
	};

	return (
		<div className='ant-table-column-sorters pointer' onClick={onHandleSort}>
			<span className='ant-table-column-title'>{title}</span>

			<div className='ant-table-column-sorter ant-table-column-sorter-full'>
				<span className='ant-table-column-sorter-inner'>
					<CaretUpOutlined
						role='presentation'
						className={`ant-table-column-sorter-up ${
							sortBy === dataIndex && sortDir === 'asc' ? 'active' : ''
						}`}
					/>
					<CaretDownOutlined
						role='presentation'
						className={`ant-table-column-sorter-down ${
							sortBy === dataIndex && sortDir === 'desc' ? 'active' : ''
						}`}
					/>
				</span>
			</div>
		</div>
	);
};

export default CommonHeaderSort;