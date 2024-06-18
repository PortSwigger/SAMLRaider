package livetesting;

import application.SamlMessageAnalyzer;
import burp.api.montoya.http.message.requests.HttpRequest;

public class SamlMessageAnalyzerTest {

    private final String rawRequest = """
            POST /api/oauth/saml HTTP/1.1
            Host: sso.eu.boxyhq.com
            Content-Length: 9984
            Cache-Control: max-age=0
            Sec-Ch-Ua: "Chromium";v="125", "Not.A/Brand";v="24"
            Sec-Ch-Ua-Mobile: ?0
            Sec-Ch-Ua-Platform: "Linux"
            Upgrade-Insecure-Requests: 1
            Origin: https://mocksaml.com
            Content-Type: application/x-www-form-urlencoded
            User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.6422.112 Safari/537.36
            Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
            Sec-Fetch-Site: cross-site
            Sec-Fetch-Mode: navigate
            Sec-Fetch-User: ?1
            Sec-Fetch-Dest: document
            Referer: https://mocksaml.com/
            Accept-Encoding: gzip, deflate, br
            Accept-Language: en-GB,en-US;q=0.9,en;q=0.8
            Priority: u=0, i
            Connection: keep-alive
                            
            RelayState=undefined&SAMLResponse=PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48c2FtbHA6UmVzcG9uc2UgRGVzdGluYXRpb249Imh0dHBzOi8vc3NvLmV1LmJveHlocS5jb20vYXBpL29hdXRoL3NhbWwiIElEPSJfNjhiYmM3NDlmM2Q5Nzc4Yzg4MDAiIElzc3VlSW5zdGFudD0iMjAyNC0wNi0xN1QwODo0OTo1NS40NzhaIiBWZXJzaW9uPSIyLjAiIHhtbG5zOnNhbWxwPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6cHJvdG9jb2wiIHhtbG5zOnhzPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYSI%2BPHNhbWw6SXNzdWVyIEZvcm1hdD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOm5hbWVpZC1mb3JtYXQ6ZW50aXR5IiB4bWxuczpzYW1sPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXNzZXJ0aW9uIj5odHRwczovL3NhbWwuZXhhbXBsZS5jb20vZW50aXR5aWQ8L3NhbWw6SXNzdWVyPjxTaWduYXR1cmUgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyMiPjxTaWduZWRJbmZvPjxDYW5vbmljYWxpemF0aW9uTWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8xMC94bWwtZXhjLWMxNG4jIi8%2BPFNpZ25hdHVyZU1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZHNpZy1tb3JlI3JzYS1zaGEyNTYiLz48UmVmZXJlbmNlIFVSST0iI182OGJiYzc0OWYzZDk3NzhjODgwMCI%2BPFRyYW5zZm9ybXM%2BPFRyYW5zZm9ybSBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyNlbnZlbG9wZWQtc2lnbmF0dXJlIi8%2BPFRyYW5zZm9ybSBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMTAveG1sLWV4Yy1jMTRuIyIvPjwvVHJhbnNmb3Jtcz48RGlnZXN0TWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8wNC94bWxlbmMjc2hhMjU2Ii8%2BPERpZ2VzdFZhbHVlPlJXdzlOL2pVK2w5WldUUjZ2dDJxeDNSYm1lNERyY1lvakFCb3I1RjRNdVE9PC9EaWdlc3RWYWx1ZT48L1JlZmVyZW5jZT48L1NpZ25lZEluZm8%2BPFNpZ25hdHVyZVZhbHVlPkdxa0FOY3FyWGZTNDF3dDhMQXRIVDAwWFpUVFZUQlhJSTZHSzZlaldIUVVkMkMrL2cwcjZHSWh0bmxyWDB4U1hpNUtQdG1yV1JiTEpjWU5VVW4rWEx5L21Ic2VLaDBlWXJVWXQxaDA2WHVMSk5NQitMSjBGZGxqSzEzZUZxOFFTR2lWemRWd1lKVWtnZjFNNlByUXZLNVVMOVpiaXhCU2VIcFFaS1FWYjNPN3grNG5UOG5Da3NuUFFjaElxME9Za1plOWhYTW9FZ2hDSG1jQ3Qwa2ZaZjAwMWdJSmZVTGhLM3R1c25KSGM3aktTL21oNVQ5MWlvUWpkNy9qUzBRcUVpWDFnMjdvUzEwaEhONDlLK0d2ZVdGOXB4U2kwaDFrVG52dzFlc0NnYTBud25QVm9QRGJJelcxS1hXTXJEajVyd2VEVHZ5WmFBRGs4WUJQMFNzR1hLdz09PC9TaWduYXR1cmVWYWx1ZT48S2V5SW5mbz48WDUwOURhdGE%2BPFg1MDlDZXJ0aWZpY2F0ZT5NSUlDNGpDQ0Fjb0NDUUMzM3dueWJUNVFaREFOQmdrcWhraUc5dzBCQVFzRkFEQXlNUXN3Q1FZRFZRUUdFd0pWClN6RVBNQTBHQTFVRUNnd0dRbTk0ZVVoUk1SSXdFQVlEVlFRRERBbE5iMk5ySUZOQlRVd3dJQmNOTWpJd01qSTQKTWpFME5qTTRXaGdQTXpBeU1UQTNNREV5TVRRMk16aGFNREl4Q3pBSkJnTlZCQVlUQWxWTE1ROHdEUVlEVlFRSwpEQVpDYjNoNVNGRXhFakFRQmdOVkJBTU1DVTF2WTJzZ1UwRk5URENDQVNJd0RRWUpLb1pJaHZjTkFRRUJCUUFECmdnRVBBRENDQVFvQ2dnRUJBTEdmWWV0dE1zY3QxVDZ0VlV3VHVkTkpINVBuYjlHR25rWGk5WncvZTZ4NDVERDAKUnVST05iRmxKMlQ0UmpBRS91RytBalh4WFE4bzJTWmZiOStHZ21DSHVUSkZOZ0hvWjFuRlZYQ21iL0hnOEhwZAo0dk9BR1huZGl4YVJlT2lxM0VINVh2cE1qTWtKMys4KzlWWU16TVpPamtnUXRBcU8zNmVBRkZmTktYN2RUajNWCnB3TGt2ejYvS0ZDcThPQXdZK0FVaTRlWm01SjU3RDMxR3pqSHdmakg5V1RlWDBNeW5kbW5OQjFxVjc1cVFSM2IKMi9XNXNHSFJ2KzlBYXJnZ0prRitwdFVrWG9MdFZBNTF3Y2ZZbTZoSUxwdHBkZTVGUUM4UldZMVlyc3dCV0FFWgpOZnlyUjRKZVN3ZUVsTkhnNE5WT3M0VHdHak9Qd1dHcXpUZmdUbEVDQXdFQUFUQU5CZ2txaGtpRzl3MEJBUXNGCkFBT0NBUUVBQVlSbFlmbFNYQVdvWnBGZndOaUNRVkU1ZDl6WjBEUHpOZFdoQXliWGNUeU1mMHo1bURmNkZXQlcKNUd5b2k5dTNFTUVEbnpMY0pOa3dKQUFjMzlBcGE0STIvdG1sK0p5MjlkazhiVHlYNm05M25nbUNnZExoNVphNApraHVVM0FNM0w2M2c3VmV4Q3VPN2t3a2poLytMcWRjSVhzVkdPNlhEZnUyUU9zMVhwZTl6SXpMcHdtL1JOWWVYClVqYlNqNWNlL2pla3BBdzdxeVZWTDR4T3loOEF0VVcxZWszd0l3MU1KdkVnRVB0MGQxNm9zaFdKcG9TMU9UOEwKci8yMlN2WUVvM0VtU0dkVFZHZ2szeDNzK0EwcVdBcVRjeWpyN1E0cy9HS1lSRmZvbUd3ejBUWjRJdzFaTjk5TQptMGVvMlVTbFNSVFZsN1FIUlR1aXVTVGhIcExLUVE9PTwvWDUwOUNlcnRpZmljYXRlPjwvWDUwOURhdGE%2BPC9LZXlJbmZvPjwvU2lnbmF0dXJlPjxzYW1scDpTdGF0dXMgeG1sbnM6c2FtbHA9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpwcm90b2NvbCI%2BPHNhbWxwOlN0YXR1c0NvZGUgVmFsdWU9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpzdGF0dXM6U3VjY2VzcyIvPjwvc2FtbHA6U3RhdHVzPjxzYW1sOkFzc2VydGlvbiBJRD0iX2ZmYzM2NmY2ZTgxMWVjNjA3YTA1IiBJc3N1ZUluc3RhbnQ9IjIwMjQtMDYtMTdUMDg6NDk6NTUuNDc4WiIgVmVyc2lvbj0iMi4wIiB4bWxuczpzYW1sPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXNzZXJ0aW9uIiB4bWxuczp4cz0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEiPjxzYW1sOklzc3VlciBGb3JtYXQ9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpuYW1laWQtZm9ybWF0OmVudGl0eSIgeG1sbnM6c2FtbD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmFzc2VydGlvbiI%2BaHR0cHM6Ly9zYW1sLmV4YW1wbGUuY29tL2VudGl0eWlkPC9zYW1sOklzc3Vlcj48U2lnbmF0dXJlIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjIj48U2lnbmVkSW5mbz48Q2Fub25pY2FsaXphdGlvbk1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMTAveG1sLWV4Yy1jMTRuIyIvPjxTaWduYXR1cmVNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGRzaWctbW9yZSNyc2Etc2hhMjU2Ii8%2BPFJlZmVyZW5jZSBVUkk9IiNfZmZjMzY2ZjZlODExZWM2MDdhMDUiPjxUcmFuc2Zvcm1zPjxUcmFuc2Zvcm0gQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjZW52ZWxvcGVkLXNpZ25hdHVyZSIvPjxUcmFuc2Zvcm0gQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzEwL3htbC1leGMtYzE0biMiLz48L1RyYW5zZm9ybXM%2BPERpZ2VzdE1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZW5jI3NoYTI1NiIvPjxEaWdlc3RWYWx1ZT54a1NLdTRyeTFkUkw0cHlZb1RXR3hBNnc0bHBybVkybkxiemk1TXM3Vkw4PTwvRGlnZXN0VmFsdWU%2BPC9SZWZlcmVuY2U%2BPC9TaWduZWRJbmZvPjxTaWduYXR1cmVWYWx1ZT5laVh3b0hJRWJjN3paMHZ6YnhXdHNORXNaWDZsRnlNMTZQbXlKWk9mR3dkN1hqaSs5WXRTeDJxeGcxOWNaMUFnL1JzS3FNc1R5SmxOaUVMNU9CN0lzdGF6dmU4K29QY050ZXNnMEpBWTg1SHFRQnZXYmRxem9tSkF2OUVRakg3SkpBQk80azM4eUwvYWtLM3Z4Ky8yNWc3K2dESWNyNUxHc2thQkVTdnlDeTA2dVVtSjNVdDlTMWpIbzVkV3BZeEEvK3VKUmVBcHVGWE1mYUhpdmZ3bkhocldlZ3J4VDY4bGFnUXdzQU42NStTV0p3TFdlVDdLVk5XWVJGZUNweDVOWEpvaUovRkcxcm94MndSSUtRZHVDeHFpT0FDcXdnY0F5M1BDaExWK0k5Q3g1aUFZc0N2ZXZZVUhPcXNpU1JpNUVmeWVZWEtIeHV4dmRwYkJnTU43Wmc9PTwvU2lnbmF0dXJlVmFsdWU%2BPEtleUluZm8%2BPFg1MDlEYXRhPjxYNTA5Q2VydGlmaWNhdGU%2BTUlJQzRqQ0NBY29DQ1FDMzN3bnliVDVRWkRBTkJna3Foa2lHOXcwQkFRc0ZBREF5TVFzd0NRWURWUVFHRXdKVgpTekVQTUEwR0ExVUVDZ3dHUW05NGVVaFJNUkl3RUFZRFZRUUREQWxOYjJOcklGTkJUVXd3SUJjTk1qSXdNakk0Ck1qRTBOak00V2hnUE16QXlNVEEzTURFeU1UUTJNemhhTURJeEN6QUpCZ05WQkFZVEFsVkxNUTh3RFFZRFZRUUsKREFaQ2IzaDVTRkV4RWpBUUJnTlZCQU1NQ1Uxdlkyc2dVMEZOVERDQ0FTSXdEUVlKS29aSWh2Y05BUUVCQlFBRApnZ0VQQURDQ0FRb0NnZ0VCQUxHZllldHRNc2N0MVQ2dFZVd1R1ZE5KSDVQbmI5R0dua1hpOVp3L2U2eDQ1REQwClJ1Uk9OYkZsSjJUNFJqQUUvdUcrQWpYeFhROG8yU1pmYjkrR2dtQ0h1VEpGTmdIb1oxbkZWWENtYi9IZzhIcGQKNHZPQUdYbmRpeGFSZU9pcTNFSDVYdnBNak1rSjMrOCs5VllNek1aT2prZ1F0QXFPMzZlQUZGZk5LWDdkVGozVgpwd0xrdno2L0tGQ3E4T0F3WStBVWk0ZVptNUo1N0QzMUd6akh3ZmpIOVdUZVgwTXluZG1uTkIxcVY3NXFRUjNiCjIvVzVzR0hSdis5QWFyZ2dKa0YrcHRVa1hvTHRWQTUxd2NmWW02aElMcHRwZGU1RlFDOFJXWTFZcnN3QldBRVoKTmZ5clI0SmVTd2VFbE5IZzROVk9zNFR3R2pPUHdXR3F6VGZnVGxFQ0F3RUFBVEFOQmdrcWhraUc5dzBCQVFzRgpBQU9DQVFFQUFZUmxZZmxTWEFXb1pwRmZ3TmlDUVZFNWQ5elowRFB6TmRXaEF5YlhjVHlNZjB6NW1EZjZGV0JXCjVHeW9pOXUzRU1FRG56TGNKTmt3SkFBYzM5QXBhNEkyL3RtbCtKeTI5ZGs4YlR5WDZtOTNuZ21DZ2RMaDVaYTQKa2h1VTNBTTNMNjNnN1ZleEN1Tzdrd2tqaC8rTHFkY0lYc1ZHTzZYRGZ1MlFPczFYcGU5ekl6THB3bS9STlllWApVamJTajVjZS9qZWtwQXc3cXlWVkw0eE95aDhBdFVXMWVrM3dJdzFNSnZFZ0VQdDBkMTZvc2hXSnBvUzFPVDhMCnIvMjJTdllFbzNFbVNHZFRWR2drM3gzcytBMHFXQXFUY3lqcjdRNHMvR0tZUkZmb21Hd3owVFo0SXcxWk45OU0KbTBlbzJVU2xTUlRWbDdRSFJUdWl1U1RoSHBMS1FRPT08L1g1MDlDZXJ0aWZpY2F0ZT48L1g1MDlEYXRhPjwvS2V5SW5mbz48L1NpZ25hdHVyZT48c2FtbDpTdWJqZWN0IHhtbG5zOnNhbWw9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphc3NlcnRpb24iPjxzYW1sOk5hbWVJRCBGb3JtYXQ9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjEuMTpuYW1laWQtZm9ybWF0OmVtYWlsQWRkcmVzcyI%2BamFja3NvbkBleGFtcGxlLmNvbTwvc2FtbDpOYW1lSUQ%2BPHNhbWw6U3ViamVjdENvbmZpcm1hdGlvbiBNZXRob2Q9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpjbTpiZWFyZXIiPjxzYW1sOlN1YmplY3RDb25maXJtYXRpb25EYXRhIE5vdE9uT3JBZnRlcj0iMjAyNC0wNi0xN1QwODo1NDo1NS40NzhaIiBSZWNpcGllbnQ9Imh0dHBzOi8vc3NvLmV1LmJveHlocS5jb20vYXBpL29hdXRoL3NhbWwiLz48L3NhbWw6U3ViamVjdENvbmZpcm1hdGlvbj48L3NhbWw6U3ViamVjdD48c2FtbDpDb25kaXRpb25zIE5vdEJlZm9yZT0iMjAyNC0wNi0xN1QwODo0NDo1NS40NzhaIiBOb3RPbk9yQWZ0ZXI9IjIwMjQtMDYtMTdUMDg6NTQ6NTUuNDc4WiIgeG1sbnM6c2FtbD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmFzc2VydGlvbiI%2BPHNhbWw6QXVkaWVuY2VSZXN0cmljdGlvbj48c2FtbDpBdWRpZW5jZT5odHRwczovL3NhbWwuYm94eWhxLmNvbTwvc2FtbDpBdWRpZW5jZT48L3NhbWw6QXVkaWVuY2VSZXN0cmljdGlvbj48L3NhbWw6Q29uZGl0aW9ucz48c2FtbDpBdXRoblN0YXRlbWVudCBBdXRobkluc3RhbnQ9IjIwMjQtMDYtMTdUMDg6NDk6NTUuNDc4WiIgeG1sbnM6c2FtbD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmFzc2VydGlvbiI%2BPHNhbWw6QXV0aG5Db250ZXh0PjxzYW1sOkF1dGhuQ29udGV4dENsYXNzUmVmPnVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphYzpjbGFzc2VzOlBhc3N3b3JkUHJvdGVjdGVkVHJhbnNwb3J0PC9zYW1sOkF1dGhuQ29udGV4dENsYXNzUmVmPjwvc2FtbDpBdXRobkNvbnRleHQ%2BPC9zYW1sOkF1dGhuU3RhdGVtZW50PjxzYW1sOkF0dHJpYnV0ZVN0YXRlbWVudCB4bWxuczpzYW1sPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXNzZXJ0aW9uIj48c2FtbDpBdHRyaWJ1dGUgTmFtZT0iaWQiIE5hbWVGb3JtYXQ9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDphdHRybmFtZS1mb3JtYXQ6dW5zcGVjaWZpZWQiPjxzYW1sOkF0dHJpYnV0ZVZhbHVlIHhtbG5zOnhzPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYSIgeG1sbnM6eHNpPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYS1pbnN0YW5jZSIgeHNpOnR5cGU9InhzOnN0cmluZyI%2BMWRkYTlmYjQ5MWRjMDFiZDI0ZDI0MjNiYTJmMjJhZTU2MWY1NmRkZjIzNzZiMjlhMTFjODAyODFkMjEyMDFmOTwvc2FtbDpBdHRyaWJ1dGVWYWx1ZT48L3NhbWw6QXR0cmlidXRlPjxzYW1sOkF0dHJpYnV0ZSBOYW1lPSJlbWFpbCIgTmFtZUZvcm1hdD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmF0dHJuYW1lLWZvcm1hdDp1bnNwZWNpZmllZCI%2BPHNhbWw6QXR0cmlidXRlVmFsdWUgeG1sbnM6eHM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvWE1MU2NoZW1hIiB4bWxuczp4c2k9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvWE1MU2NoZW1hLWluc3RhbmNlIiB4c2k6dHlwZT0ieHM6c3RyaW5nIj5qYWNrc29uQGV4YW1wbGUuY29tPC9zYW1sOkF0dHJpYnV0ZVZhbHVlPjwvc2FtbDpBdHRyaWJ1dGU%2BPHNhbWw6QXR0cmlidXRlIE5hbWU9ImZpcnN0TmFtZSIgTmFtZUZvcm1hdD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOmF0dHJuYW1lLWZvcm1hdDp1bnNwZWNpZmllZCI%2BPHNhbWw6QXR0cmlidXRlVmFsdWUgeG1sbnM6eHM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvWE1MU2NoZW1hIiB4bWxuczp4c2k9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvWE1MU2NoZW1hLWluc3RhbmNlIiB4c2k6dHlwZT0ieHM6c3RyaW5nIj5qYWNrc29uPC9zYW1sOkF0dHJpYnV0ZVZhbHVlPjwvc2FtbDpBdHRyaWJ1dGU%2BPHNhbWw6QXR0cmlidXRlIE5hbWU9Imxhc3ROYW1lIiBOYW1lRm9ybWF0PSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXR0cm5hbWUtZm9ybWF0OnVuc3BlY2lmaWVkIj48c2FtbDpBdHRyaWJ1dGVWYWx1ZSB4bWxuczp4cz0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEiIHhtbG5zOnhzaT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEtaW5zdGFuY2UiIHhzaTp0eXBlPSJ4czpzdHJpbmciPmphY2tzb248L3NhbWw6QXR0cmlidXRlVmFsdWU%2BPC9zYW1sOkF0dHJpYnV0ZT48L3NhbWw6QXR0cmlidXRlU3RhdGVtZW50Pjwvc2FtbDpBc3NlcnRpb24%2BPC9zYW1scDpSZXNwb25zZT4%3D""";

    public TestResult isSAMLMessage() {
        try {
            var request = HttpRequest.httpRequest(rawRequest);
            var analysis = SamlMessageAnalyzer.analyze(request, "SAMLRequest", "SAMLResponse");
            var success = analysis.isSAMLMessage();
            return new TestResult(success, null, null);
        } catch (Exception exc) {
            return new TestResult(false, null, exc);
        }
    }

}
