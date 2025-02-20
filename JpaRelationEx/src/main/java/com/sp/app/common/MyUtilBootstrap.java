package com.sp.app.common;

import org.springframework.stereotype.Service;

@Service("myUtil")
public class MyUtilBootstrap extends MyUtil {
	// 페이징(paging) 처리(GET 방식) : 부트스트랩
	@Override
	public String paging(int current_page, int total_page, String list_url) {
		StringBuilder sb = new StringBuilder();

		int numPerBlock = 10;
		int currentPageSetup;
		int n, page;

		if (current_page < 1 || total_page < current_page) {
			return "";
		}

		if (list_url.indexOf("?") != -1) {
			list_url += "&";
		} else {
			list_url += "?";
		}

		// currentPageSetup : 표시할첫페이지-1
		currentPageSetup = (current_page / numPerBlock) * numPerBlock;
		if (current_page % numPerBlock == 0) {
			currentPageSetup = currentPageSetup - numPerBlock;
		}

		sb.append("<ul class='pagination justify-content-center'>");
		// 처음페이지
		if (current_page > 1) {
			sb.append("<li class='page-item'><a class='page-link' href='" + list_url
					+ "page=1' aria-label='First'><i class='bi bi-chevron-bar-left'></i></a></li>");
		} else {
			sb.append("<li class='page-item disabled'><a class='page-link' href='#' aria-disabled='true'><i class='bi bi-chevron-bar-left'></i></a></li>");
		}
		// 이전(10페이지 전)
		n = current_page - numPerBlock;
		if (total_page > numPerBlock && currentPageSetup > 0) {
			sb.append("<li class='page-item'><a class='page-link' href='" + list_url + "page=" + n
					+ "' aria-label='Previous'><i class='bi bi-chevron-double-left'></i></a></li>");
		} else {
			sb.append("<li class='page-item disabled'><a class='page-link' href='#' aria-disabled='true'><i class='bi bi-chevron-double-left'></i></a></li>");
		}

		// 바로가기
		page = currentPageSetup + 1;
		while (page <= total_page && page <= (currentPageSetup + numPerBlock)) {
			if (page == current_page) {
				sb.append("<li class='page-item active'><span class='page-link'>" + page + "</span></li>");
			} else {
				sb.append("<li class='page-item'><a class='page-link' href='" + list_url + "page=" + page + "'>" + page	+ "</a></li>");
			}
			page++;
		}

		// 다음(10페이지 후)
		n = current_page + numPerBlock;
		if (n > total_page)
			n = total_page;
		if (total_page - currentPageSetup > numPerBlock) {
			sb.append("<li class='page-item'><a class='page-link' href='" + list_url + "page=" + n
					+ "' aria-label='Next'><i class='bi bi-chevron-double-right'></i></a></li>");
		} else {
			sb.append("<li class='page-item disabled'><a class='page-link' href='#' aria-disabled='true'><i class='bi bi-chevron-double-right'></i></a></li>");
		}
		// 마지막페이지
		if (current_page < total_page) {
			sb.append("<li class='page-item'><a class='page-link' href='" + list_url + "page=" + total_page
					+ "' aria-label='Last'><i class='bi bi-chevron-bar-right'></i></a></li>");
		} else {
			sb.append("<li class='page-item disabled'><a class='page-link' href='#' aria-disabled='true'><i class='bi bi-chevron-bar-right'></i></a></li>");
		}

		sb.append("</ul>");

		return sb.toString();
	}

	// javascript 페이지 처리(javascript 지정 함수 호출, methodName:호출할 스크립트 함수명) : 부트스트랩
	@Override
    public String pagingMethod(int current_page, int total_page, String methodName) {
		StringBuilder sb = new StringBuilder();

		int numPerBlock = 10; // 리스트에 나타낼 페이지 수
		int currentPageSetUp;
		int n, page;

		if (current_page < 1 || total_page < current_page) {
			return "";
		}

		// currentPageSetup : 표시할첫페이지-1
		currentPageSetUp = (current_page / numPerBlock) * numPerBlock;
		if (current_page % numPerBlock == 0) {
			currentPageSetUp = currentPageSetUp - numPerBlock;
		}

		sb.append("<ul class='pagination justify-content-center'>");

		// 처음페이지
		if (current_page > 1) {
			sb.append("<li class='page-item'><a class='page-link' onclick='" 
					+ methodName + "(1);' aria-label='First'><i class='bi bi-chevron-bar-left'></i></a></li>");
		} else {
			sb.append("<li class='page-item disabled'><a class='page-link' href='#' aria-disabled='true'><i class='bi bi-chevron-bar-left'></i></a></li>");
		}
		// 이전(10페이지 전)
		n = current_page - numPerBlock;
		if (total_page > numPerBlock && currentPageSetUp > 0) {
			sb.append("<li class='page-item'><a class='page-link' onclick='" 
					+ methodName + "(" + n + ");' aria-label='Previous'><i class='bi bi-chevron-double-left'></i></a></li>");
		} else {
			sb.append("<li class='page-item disabled'><a class='page-link'  href='#' aria-disabled='true'><i class='bi bi-chevron-double-left'></i></a></li>");
		}

		// 바로가기 페이지 구현
		page = currentPageSetUp + 1;
		while (page <= total_page && page <= (currentPageSetUp + numPerBlock)) {
			if (page == current_page) {
				sb.append("<li class='page-item active'><span class='page-link' href='#'>" + page + "</span></li>");
			} else {
				sb.append("<li class='page-item page-item'><a class='page-link' onclick='" 
						+ methodName + "(" + page + ");'>" + page + "</a></li>");
			}
			page++;
		}

		// 다음(10페이지 후)
		n = current_page + numPerBlock;
		if (n > total_page)
			n = total_page;
		if (total_page - currentPageSetUp > numPerBlock) {
			sb.append("<li class='page-item'><a class='page-link' onclick='" 
					+ methodName + "(" + n + "); aria-label='Next'><i class='bi bi-chevron-double-right'></i></a></li>");
		} else {
			sb.append("<li class='page-item disabled'><a class='page-link' href='#' aria-disabled='true'><i class='bi bi-chevron-double-right'></i></a></li>");
		}
		// 마지막 페이지
		if (current_page < total_page) {
			sb.append("<li class='page-item'><a class='page-link' onclick='" 
					+ methodName + "(" + total_page + ");' aria-label='Last'><i class='bi bi-chevron-bar-right'></i></a></li>");
		} else {
			sb.append("<li class='page-item disabled'><a class='page-link' href='#' aria-disabled='true'><i class='bi bi-chevron-bar-right'></i></a></li>");
		}

		sb.append("</ul>");

		return sb.toString();
    }
	
	// 화면에 표시할 페이지를 중앙에 출력
	@Override
	public String pagingUrl(int current_page, int total_page, String list_url) {
		StringBuilder sb = new StringBuilder();
		
		int numPerBlock = 10;
		int n, page;
		
		if(current_page < 1 || total_page < current_page) {
			return "";
		}
		
		if(list_url.indexOf("?") != -1) {
			list_url += "&";
		} else {
			list_url += "?";
		}
		
		page = 1; // 출력할 시작 페이지
		if(current_page > (numPerBlock / 2) + 1) {
			page = current_page - (numPerBlock / 2) ;
			
			n = total_page - page;
			if( n <= numPerBlock ) {
				page = total_page - numPerBlock + 1;
			}
			
			if(page < 1) page = 1;
		}
		
		sb.append("<ul class='pagination justify-content-center'>");
		
		// 처음페이지
		if(page > 1) {
			sb.append("<li class='page-item'><a class='page-link' href='" + list_url
					+ "page=1' aria-label='First'><i class='bi bi-chevron-bar-left'></i></a></li>");
		} else {
			sb.append("<li class='page-item disabled'><a class='page-link' href='#' aria-disabled='true'><i class='bi bi-chevron-bar-left'></i></a></li>");
		}

		// 이전(한페이지 전)
		n = current_page - 1;
		if(current_page > 1) {
			sb.append("<li class='page-item'><a class='page-link' href='" + list_url + "page=" + n
					+ "' aria-label='Previous'><i class='bi bi-chevron-double-left'></i></a></li>");
		} else {
			sb.append("<li class='page-item disabled'><a class='page-link' href='#' aria-disabled='true'><i class='bi bi-chevron-double-left'></i></a></li>");
		}

		n = page;
		while(page <= total_page && page < n + numPerBlock) {
			if(page == current_page) {
				sb.append("<li class='page-item active'><span class='page-link'>" + page + "</span></li>");
			} else {
				sb.append("<li class='page-item'><a class='page-link' href='" + list_url + "page=" + page + "'>" + page	+ "</a></li>");
			}
			page++;
		}

		// 다음(한페이지 다음)
		n = current_page + 1;
		if(current_page < total_page) {
			sb.append("<li class='page-item'><a class='page-link' href='" + list_url + "page=" + n
					+ "' aria-label='Next'><i class='bi bi-chevron-double-right'></i></a></li>");
		} else {
			sb.append("<li class='page-item disabled'><a class='page-link' href='#' aria-disabled='true'><i class='bi bi-chevron-double-right'></i></a></li>");
		}

		// 마지막페이지
		if(page <= total_page) {
			sb.append("<li class='page-item'><a class='page-link' href='" + list_url + "page=" + total_page
					+ "' aria-label='Last'><i class='bi bi-chevron-bar-right'></i></a></li>");
		} else {
			sb.append("<li class='page-item disabled'><a class='page-link' href='#' aria-disabled='true'><i class='bi bi-chevron-bar-right'></i></a></li>");
		}
		
		sb.append("</ul>");
		
		return sb.toString();
	}

	// 화면에 표시할 페이지를 중앙에 출력 : javascript 함수 호출
	@Override
	public String pagingFunc(int current_page, int total_page, String methodName) {
		StringBuilder sb = new StringBuilder();
		
		int numPerBlock = 10;
		int n, page;
		
		if(current_page < 1 || total_page < current_page) {
			return "";
		}
		
		page = 1; // 출력할 시작 페이지
		if(current_page > (numPerBlock / 2) + 1) {
			page = current_page - (numPerBlock / 2) ;
			
			n = total_page - page;
			if( n <= numPerBlock ) {
				page = total_page - numPerBlock + 1;
			}
			
			if(page < 1) page = 1;
		}
		
		sb.append("<ul class='pagination justify-content-center'>");
		
		// 처음페이지
		if(page > 1) {
			sb.append("<li class='page-item'><a class='page-link' onclick='" 
					+ methodName + "(1);' aria-label='First'><i class='bi bi-chevron-bar-left'></i></a></li>");
		} else {
			sb.append("<li class='page-item disabled'><a class='page-link' href='#' aria-disabled='true'><i class='bi bi-chevron-bar-left'></i></a></li>");
		}

		// 이전(한페이지 전)
		n = current_page - 1;
		if(current_page > 1) {
			sb.append("<li class='page-item'><a class='page-link' onclick='" 
					+ methodName + "(" + n + ");' aria-label='Previous'><i class='bi bi-chevron-double-left'></i></a></li>");
		} else {
			sb.append("<li class='page-item disabled'><a class='page-link'  href='#' aria-disabled='true'><i class='bi bi-chevron-double-left'></i></a></li>");
		}

		n = page;
		while(page <= total_page && page < n + numPerBlock) {
			if(page == current_page) {
				sb.append("<li class='page-item active'><span class='page-link' href='#'>" + page + "</span></li>");
			} else {
				sb.append("<li class='page-item page-item'><a class='page-link' onclick='" 
						+ methodName + "(" + page + ");'>" + page + "</a></li>");
			}
			page++;
		}

		// 다음(한페이지 다음)
		n = current_page + 1;
		if(current_page < total_page) {
			sb.append("<li class='page-item'><a class='page-link' onclick='" 
					+ methodName + "(" + n + "); aria-label='Next'><i class='bi bi-chevron-double-right'></i></a></li>");
		} else {
			sb.append("<li class='page-item disabled'><a class='page-link' href='#' aria-disabled='true'><i class='bi bi-chevron-double-right'></i></a></li>");
		}

		// 마지막페이지
		if(page <= total_page) {
			sb.append("<li class='page-item'><a class='page-link' onclick='" 
					+ methodName + "(" + total_page + ");' aria-label='Last'><i class='bi bi-chevron-bar-right'></i></a></li>");
		} else {
			sb.append("<li class='page-item disabled'><a class='page-link' href='#' aria-disabled='true'><i class='bi bi-chevron-bar-right'></i></a></li>");
		}
		
		sb.append("</ul>");
		
		return sb.toString();
	}
}
