import React, { useCallback, useEffect, useState } from 'react';
import FileSaver from 'file-saver';
import { Button, message, Table } from 'antd';
import { exportReport, getReport } from '../../services/reportService';
import CommonHeaderSort from '../../components/Common/CommonHeaderSort';

const Report = () => {
	const [loading, setLoading] = useState(false);
	const [dataSource, setDataSource] = useState([]);
	const [totalElements, setTotalElements] = useState();

	const initialFilters = {
		pageSize: 10,
		pageNo: 1,
		sortBy: 'category',
		sortDir: 'asc',
	};
	const [filter, setFilter] = useState(initialFilters);

	const getData = useCallback(() => {
		setLoading(true);
		getReport(filter)
			.then((res) => {
				const convertedData = res.data.content.map((data) => {
					const {
						categoryDto,
						total,
						assigned,
						available,
						notAvailable,
						recycled,
						waitingForRecycling,
					} = data;

					return {
						key: categoryDto.categoryId,
						category: categoryDto.name,
						total,
						assigned,
						available,
						notAvailable,
						recycled,
						waitingForRecycling,
					};
				});

				setDataSource(convertedData);
				setTotalElements(res.data.totalElements);
			})
			.catch((error) => {
				message.error(error.message);
			});
		setLoading(false);
	}, [filter]);

	const exportExcel = () => {
		exportReport().then((response) => {
			const blob = new Blob([response.data], {
				type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
			});
			FileSaver.saveAs(blob, 'Report.xlsx');
		});
	};

	useEffect(() => {
		getData();
	}, [getData]);

	const onPageChange = (pageNo, pageSize) => {
		const newFilter = {
			...filter,
			pageNo,
			pageSize,
		};
		setFilter(newFilter);
	};

	const onSort = (dataIndex, sortDir) => {
		const newFilter = {
			...filter,
			sortBy: dataIndex,
			sortDir,
		};
		setFilter(newFilter);
	};

	const columns = [
		{
			title: (
				<CommonHeaderSort
					title='Category'
					dataIndex='category'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'category',
			key: 'category',
		},
		{
			title: (
				<CommonHeaderSort
					title='Total'
					dataIndex='total'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'total',
			key: 'total',
		},
		{
			title: (
				<CommonHeaderSort
					title='Assigned'
					dataIndex='assigned'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'assigned',
			key: 'assigned',
		},
		{
			title: (
				<CommonHeaderSort
					title='Available'
					dataIndex='available'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'available',
			key: 'available',
		},
		{
			title: (
				<CommonHeaderSort
					title='Not Available'
					dataIndex='notAvailable'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'notAvailable',
			key: 'notAvailable',
		},
		{
			title: (
				<CommonHeaderSort
					title='Waiting For Recycling'
					dataIndex='waitingForRecycling'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'waitingForRecycling',
			key: 'waitingForRecycling',
		},
		{
			title: (
				<CommonHeaderSort
					title='Recycled'
					dataIndex='recycled'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'recycled',
			key: 'recycled',
		},
	];

	return (
		<>
			<h1>Report</h1>

			<div style={{ textAlign: 'right' }}>
				<Button
					style={{ marginBottom: '30px' }}
					type='primary'
					danger
					onClick={() => exportExcel()}>
					Export
				</Button>

				<Table
					columns={columns}
					dataSource={dataSource}
					loading={loading}
					pagination={{
						current: filter.pageNo,
						pageSize: filter.pageSize,
						total: totalElements,
						onChange: onPageChange,
						hideOnSinglePage: true,
					}}
				/>
			</div>
		</>
	);
};

export default Report;
