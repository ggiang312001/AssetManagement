// @ts-nocheck
import { Divider, message, Modal, Table } from 'antd';
import React, { useCallback, useEffect, useState } from 'react';
import { getAssetDetail } from '../../services/assetService';
import { getAssignments, getReturnReq } from '../../services/assignmentService';

const AssetDetailModal = ({ assetId, isModalOpen, setIsModalOpen }) => {
	const [assetDetail, setAssetDetail] = useState([]);
	function titleCase(str) {
		var splitStr = str.toLowerCase().split(' ');
		for (var i = 0; i < splitStr.length; i++) {
			// You do not need to check if i is larger than splitStr length, as your for does that for you
			// Assign it back to the array
			splitStr[i] =
				splitStr[i].charAt(0).toUpperCase() + splitStr[i].substring(1);
		}
		// Directly return the joined string
		return splitStr.join(' ');
	}
	const initialFilters = {
		searchTerm: '',
		dateFill: '',
		stateFill: '',
		pageSize: 3,
		pageNo: 1,
		sortBy: 'assignmentId',
		sortDir: 'asc',
	};
	const [filter, setFilter] = useState(initialFilters);
	const [assignmentData, setAssignmentData] = useState([]);
	const [totalElements, setTotalElements] = useState();

	const getDetail = useCallback(() => {
		getAssetDetail(assetId)
			.then((res) => {
				const {
					assetId,
					name,
					category,
					specification,
					installedDate,
					state,
					locationId,
				} = res.data;
				setAssetDetail([
					{ key: 'assetId', label: 'Asset Code', details: assetId },
					{ key: 'assetName', label: 'Asset Name', details: name },
					{ key: 'category', label: 'Category', details: category.name },
					{
						key: 'specification',
						label: 'Specification',
						details: specification,
					},
					{
						key: 'installedDate',
						label: 'Installed Date',
						details: installedDate.split('T')[0],
					},
					{
						key: 'state',
						label: 'State',
						details: titleCase(state.replaceAll('_', ' ')),
					},
					{ key: 'location', label: 'Location', details: locationId.city },
				]);
			})
			.catch((error) => {
				message.error(error.message);
			});
	}, [assetId]);

	const getAssignmentDetail = useCallback(() => {
		console.log(assetId)
		getReturnReq(assetId)
			.then((res) => {
				console.log(res)
				const convertedData = res.data.map((data) => {
					const { assignmentId, assignee, assigner, assignDate, state } = data;

					return {
						key: assignmentId,
						//assetId:`${assignmentId.assetId.assetId}`,
						assignmentId,
						assigner: `${assigner.username}`,
						assignee: `${assignee.username}`,
						assignDate,
						state: titleCase(state.replaceAll('_', ' ')),
					};
				});
				setAssignmentData(convertedData);
				setTotalElements(res.data.totalElements);
			})
			.catch((error) => {
				message.error(error.message);
			});
	}, [assetId]);

	useEffect(() => {
		if (assetId) {
			getDetail();
		}
	}, [getDetail, assetId]);

	useEffect(() => {
		if (assetId) {
			getAssignmentDetail();
		}
	}, [getAssignmentDetail, assetId]);

	const handleCancel = () => {
		setIsModalOpen(false);
	};

	const onPageChange = (pageNo, pageSize) => {
		const newFilter = {
			...filter,
			pageNo,
			pageSize,
		};
		setFilter(newFilter);
	};

	const assetColumns = [
		{
			title: 'Label',
			dataIndex: 'label',
			key: 'label',
		},
		{
			title: 'Details',
			dataIndex: 'details',
			key: 'details',
		},
	];

	const assignmentColumns = [
		{
			title: 'Assigned to',
			dataIndex: 'assignee',
			key: assignmentData.assignee,
			sorter: (a, b) => a.assignee.localeCompare(b.assignee),
		},
		{
			title: 'Assigned by',
			dataIndex: 'assigner',
			key: assignmentData.assigner,
			sorter: (a, b) => a.assigner.localeCompare(b.assigner),
		},
		{
			title: 'Assigned Date',
			dataIndex: 'assignDate',
			key: assignmentData.assignDate,
			sorter: (a, b) => a.assignDate.localeCompare(b.assignDate),
		},
		{
			title: 'State',
			dataIndex: 'state',
			key: assignmentData.state,
			sorter: (a, b) => a.state.localeCompare(b.state),
		},
	];

	return (
		<Modal
			title='Detailed Asset Information'
			open={isModalOpen}
			onCancel={handleCancel}
			footer={null}
			width={700}>
			<Table
				columns={assetColumns}
				dataSource={assetDetail}
				size='small'
				pagination={{
					hideOnSinglePage: true,
				}}
			/>

			<Divider style={{ marginTop: '40px', color: '#cf2338' }}>
				Assignment History
			</Divider>
			<Table
				columns={assignmentColumns}
				dataSource={assignmentData}
				size='small'
				pagination={{
					current: filter.pageNo,
					pageSize: filter.pageSize,
					total: totalElements,
					onChange: onPageChange,
					hideOnSinglePage: true,
				}}
				showSorterTooltip={false}
			/>
		</Modal>
	);
};

export default AssetDetailModal;
